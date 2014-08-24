package net.devmond.shell.handler;

import static net.devmond.shell.handler.TextResult.textResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.framework.console.CommandInterpreter;

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
}
