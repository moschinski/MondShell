package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.devmond.shell.CommandInput;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * TODO add support for saving before performing a refresh
 */
public class RefreshCommandHandler extends AbstractCommandHandler
{
	private static final Logger log = Logger.getLogger(RefreshCommandHandler.class.getName());

	private static final int MAX_CONCURRENT_REFRESHS = 5;
	private final AtomicInteger refreshCnt;

	public RefreshCommandHandler(CommandInterpreter cmdInterpreter)
	{
		super(cmdInterpreter);
		refreshCnt = new AtomicInteger(0);
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{

		IProject[] projects;
		if (cmdInput.hasNextArgument())
		{
			List<String> projectsToRefresh = new ArrayList<String>();
			while (cmdInput.hasNextArgument())
			{
				projectsToRefresh.add(cmdInput.nextArgument());
			}

			projects = getProjectResourcesFromName(projectsToRefresh.toArray(new String[projectsToRefresh.size()]));
			if (projects.length == 0)
			{
				return textResult(String.format("Found no project for the name(s) '%s' - cannot refresh",
						projectsToRefresh));
			}
		} else
		{
			projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		}

		// not sure if this is still required in current Eclipse releases
		final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REFRESHS);
		for (final IProject project : projects)
		{
			if (project.isOpen())
			{
				semaphore.tryAcquire(1, TimeUnit.MINUTES);
				if (log.isLoggable(Level.FINE))
				{
					log.fine("Start refresh of project " + project.getName());
				}

				refreshProject(refreshCnt, semaphore, project);
			}
		}

		// make sure that all refreshes completed
		semaphore.tryAcquire(MAX_CONCURRENT_REFRESHS, 1, TimeUnit.SECONDS);
		return textResult(String.format("Successfully refreshed %d projects", refreshCnt.get()));
	}

	private IProject[] getProjectResourcesFromName(String[] projectNames)
	{
		IProject[] projects = new IProject[projectNames.length];

		for (int i = 0; i < projectNames.length; i++)
		{
			projects[i] = ResourcesPlugin.getWorkspace().getRoot().getProject(projectNames[i]);
		}

		return projects;
	}

	private void refreshProject(final AtomicInteger refreshCnt, final Semaphore semaphore, final IProject project)
			throws CoreException
	{

		Display.getDefault().asyncExec(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					PlatformUI.getWorkbench().getProgressService().run(false, false, new IRunnableWithProgress()
					{

						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException
						{
							try
							{
								project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
							} catch (CoreException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						// TODO Auto-generated method stub

					});
				} catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
