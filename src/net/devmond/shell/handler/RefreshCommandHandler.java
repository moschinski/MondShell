package net.devmond.shell.handler;

import static java.util.concurrent.TimeUnit.MINUTES;
import static net.devmond.shell.handler.TextResult.textResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.devmond.shell.CommandInput;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.LoadTargetDefinitionJob;
import org.eclipse.pde.internal.core.PDECore;

/**
 * TODO add support for saving before performing a refresh
 */
public class RefreshCommandHandler extends AbstractCommandHandler
{
	private static final Logger log = Logger.getLogger(RefreshCommandHandler.class.getName());

	public RefreshCommandHandler(CommandInterpreter cmdInterpreter)
	{
		super(cmdInterpreter);
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		Collection<String> projectsToRefresh = cmdInput.getArguments();
		if (projectsToRefresh.isEmpty())
		{
			return refreshWorkspace();
		}
		return refreshProjectsByName(projectsToRefresh);
	}

	private Result refreshWorkspace() throws InterruptedException
	{
		refreshTargetPlatform();
		// refresh whole workspace
		refreshResource(ResourcesPlugin.getWorkspace().getRoot(), "Refreshing workspace");
		return textResult("Triggered refresh of workspace");
	}

	private Result refreshProjectsByName(Collection<String> projectsToRefresh) throws InterruptedException,
			CoreException
	{
		Collection<IProject> projects = getProjectsByNames(projectsToRefresh);
		if (projects.isEmpty())
		{
			return textResult("Found no projects to refresh");
		}
		return refreshProjects(projects);
	}

	private Collection<IProject> getProjectsByNames(Iterable<String> projectsToRefresh)
	{
		Set<IProject> projects = new HashSet<>();
		for (String projectName : projectsToRefresh)
		{
			projects.add(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName));
		}
		return projects;
	}

	private Result refreshProjects(Iterable<IProject> projects) throws InterruptedException, CoreException
	{
		int i = 0;
		for (final IProject project : projects)
		{
			if (project.isOpen())
			{
				i++;
				String jobName = "Refreshing project " + project.getName();
				refreshResource(project, jobName);
			}
		}
		return textResult(String.format("Scheduled the refresh of %d projects", i));
	}

	private void refreshResource(final IResource resource, String jobName) throws InterruptedException
	{
		Job job = new Job(jobName)
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try
				{
					ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
					{
						@Override
						public void run(IProgressMonitor monitor) throws CoreException
						{
							resource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
						}
					}, monitor);
					return Status.OK_STATUS;
				} catch (CoreException e)
				{
					String errorMsg = String.format("An exception happened while refreshing '%s'", resource.getName());
					log.log(Level.WARNING, errorMsg, e);
					return new Status(IStatus.ERROR, "MondShell", errorMsg);
				}
			}
		};
		job.setUser(true);
		job.setPriority(Job.LONG);
		job.schedule();
	}

	/**
	 * todo: implement a non-blocking version
	 */
	private void refreshTargetPlatform()
	{
		@SuppressWarnings("restriction")
		ITargetPlatformService platfomService = (ITargetPlatformService) PDECore.getDefault().acquireService(
				ITargetPlatformService.class.getName());
		ITargetDefinition activeTarget = null;
		try
		{
			activeTarget = platfomService.getWorkspaceTargetDefinition();

			// copy target before compare (?)
			activeTarget.resolve(new NullProgressMonitor());
			if (!activeTarget.isResolved() || activeTarget.getStatus().getSeverity() == IStatus.ERROR)
			{
				return;
			}
			if (!platfomService.compareWithTargetPlatform(activeTarget).isOK())
			{
				final CountDownLatch loaded = new CountDownLatch(1);
				LoadTargetDefinitionJob.load(activeTarget, new JobChangeAdapter()
				{

					@Override
					public void done(IJobChangeEvent event)
					{
						loaded.countDown();
					}

				});
				loaded.await(1, MINUTES);
			}
		} catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		} catch (RuntimeException | CoreException e)
		{
			log.warning("Failed to reload target platform: " + activeTarget == null ? "<unknown>" : activeTarget
					.getName());
		}
	}
}
