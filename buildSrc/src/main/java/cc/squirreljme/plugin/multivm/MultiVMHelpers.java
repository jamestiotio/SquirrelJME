// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

/**
 * Helpers for the multi-VM handlers.
 *
 * @since 2020/08/07
 */
public final class MultiVMHelpers
{
	/** Main configurations. */
	private static final String[] _MAIN_CONFIGS =
		new String[]{"api", "implementation"};
	
	/** Test configurations. */
	private static final String[] _TEST_CONFIGS =
		new String[]{"testApi", "testImplementation"};
	
	/* Copy buffer size. */
	public static final int COPY_BUFFER =
		4096;
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private MultiVMHelpers()
	{
	}
	
	/**
	 * Returns a collection of the tests that are available.
	 * 
	 * @param __project The project to check.
	 * @param __sourceSet The source set for the project.
	 * @return The list of available tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Collection<String> availableTests(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the cache directory of the project.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __vmType The virtual machine being used.
	 * @param __sourceSet The source set for the library, as there might be
	 * duplicates between them potentially.
	 * @return The path provider to the project cache directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Provider<Path> cacheDir(Project __project,
		VirtualMachineSpecifier __vmType, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> __project.getBuildDir().toPath()
			.resolve("squirreljme").resolve("vm-" + __sourceSet + "-" +
				__vmType.vmName(VMNameFormat.LOWERCASE)));
	}
	
	/**
	 * Copies from the input into the output.
	 * 
	 * @param __in The input.
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static void copy(InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		byte[] buf = new byte[MultiVMHelpers.COPY_BUFFER];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				return;
			
			__out.write(buf, 0, rc);
		}
	}
	
	/**
	 * Returns the task that creates the JAR.
	 * 
	 * @param __project The project to get from.
	 * @param __sourceSet The source set.
	 * @return The jar task used, or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Jar jarTask(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		switch (__sourceSet)
		{
			case SourceSet.MAIN_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("jar");
			
			case SourceSet.TEST_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("testJar");
			
			default:
				throw new IllegalStateException("Unknown sourceSet: " +
					__sourceSet);
		}
	}
	
	/**
	 * Resolves tasks from the projects and tasks.
	 * 
	 * @param <T> The class to resolve as.
	 * @param __class The class to resolve as.
	 * @param __project The project to latch onto for lookup.
	 * @param __in The input project and task names.
	 * @return An iterable which has the projects resolved.
	 * @throws NullPointerException On null arguments.
	 */
	public static <T extends Task> Iterable<T> resolveProjectTasks(
		Class<T> __class, Project __project, Iterable<ProjectAndTaskName> __in)
		throws NullPointerException
	{
		if (__project == null || __in == null)
			throw new NullPointerException("NARG");
		
		Collection<T> result = new LinkedList<>();
		
		// Map projects and tasks back into tasks
		for (ProjectAndTaskName depend : __in)
			result.add(__class.cast(__project.project(depend.project)
				.getTasks().getByName(depend.task)));
		
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 * 
	 * @param __project The task to get from.
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine information.
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, String __sourceSet,
		VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		return MultiVMHelpers.runClassTasks(__project, __sourceSet, __vmType,
			null);
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 * 
	 * @param __project The task to get from.
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine information.
	 * @param __did Projects that have been processed.
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, String __sourceSet,
		VirtualMachineSpecifier __vmType, Set<ProjectAndTaskName> __did)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// Make sure this is always set
		if (__did == null)
			__did = new HashSet<>();
		
		// If this process was already processed, ignore it
		ProjectAndTaskName selfProjectTask = ProjectAndTaskName.of(__project,
			TaskInitialization.task("lib", __sourceSet, __vmType));
		if (__did.contains(selfProjectTask))
			return Collections.emptySet();
		
		Set<ProjectAndTaskName> result = new LinkedHashSet<>();
		
		// Debug
		System.err.printf("DEBUG -- Eval: %s (%s-%s)%n",
			__project.getPath(), __sourceSet,
			__vmType.vmName(VMNameFormat.PROPER_NOUN));
		
		// If we are testing then we depend on the main TAC library, otherwise
		// we will not be able to do any actual testing
		if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
		{
			// Depend on TAC
			result.addAll(MultiVMHelpers.runClassTasks(
				__project.findProject(":modules:tac"),
				SourceSet.MAIN_SOURCE_SET_NAME, __vmType, __did));
			
			// Depend on our main project as we will be testing it
			result.addAll(MultiVMHelpers.runClassTasks(__project,
				SourceSet.MAIN_SOURCE_SET_NAME, __vmType, __did));
		}
		
		// Go through the configurations to yank in the dependencies as needed
		for (String config : MultiVMHelpers._MAIN_CONFIGS)
		{
			// The configuration may be optional
			Configuration foundConfig = __project.getConfigurations()
				.findByName(config);
			if (foundConfig == null)
				continue;
			
			// Handle dependencies
			for (Dependency depend : foundConfig.getDependencies())
			{
				// Only consider projects
				if (!(depend instanceof ProjectDependency))
					continue;
				
				Project sub = ((ProjectDependency)depend)
					.getDependencyProject();
				result.addAll(MultiVMHelpers.runClassTasks(sub, 
					SourceSet.MAIN_SOURCE_SET_NAME, __vmType, __did));
			}
		}
		
		// Finally add our own library for usages
		result.add(selfProjectTask);
		
		// Ignore our own project
		__did.add(selfProjectTask);
		
		// Debug
		System.err.printf("DEBUG -- Deps: %s (%s-%s) -> %s%n",
			__project.getPath(), __sourceSet,
			__vmType.vmName(VMNameFormat.PROPER_NOUN), result);
		
		return Collections.unmodifiableCollection(result);
	}
}
