begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|project
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
operator|.
name|ConfigStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|project
operator|.
name|validator
operator|.
name|Validator
import|;
end_import

begin_comment
comment|/**  * Describes a model of Cayenne project. Project is a set of files in the filesystem  * describing storing Cayenne DataMaps, DataNodes and other information.  *<p>  * Project has a project directory, which is a canonical directory. All project files are  * relative to the project directory.  *</p>  *   * @deprecated since 3.1 - use org.apache.cayenne.project2 module for projects  *             manipulation.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Project
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CURRENT_PROJECT_VERSION
init|=
literal|"3.0.0.1"
decl_stmt|;
specifier|protected
name|File
name|projectDir
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectFile
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|upgradeMessages
decl_stmt|;
specifier|protected
name|boolean
name|modified
decl_stmt|;
comment|/**      * Factory method to create the right project type given project file.      */
specifier|public
specifier|static
name|Project
name|createProject
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|String
name|fileName
init|=
name|projectFile
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|endsWith
argument_list|(
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
condition|)
block|{
return|return
operator|new
name|ApplicationProject
argument_list|(
name|projectFile
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Unsupported project file: "
operator|+
name|projectFile
argument_list|)
throw|;
block|}
block|}
comment|/**      * @since 1.2      */
specifier|protected
name|Project
parameter_list|()
block|{
block|}
comment|/**      * Constructor for Project.<code>projectFile</code> must denote a file (existent or      * non-existent) in an existing directory. If projectFile has no parent directory,      * current directory is assumed.      */
specifier|public
name|Project
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|initialize
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
name|postInitialize
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|protected
name|void
name|initialize
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
if|if
condition|(
name|projectFile
operator|!=
literal|null
condition|)
block|{
name|File
name|parent
init|=
name|projectFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|parent
operator|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|parent
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Project directory does not exist or is not a directory: "
operator|+
name|parent
argument_list|)
throw|;
block|}
try|try
block|{
name|projectDir
operator|=
name|parent
operator|.
name|getCanonicalFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Error creating project."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Finished project initialization. Called from constructor. Default implementation      * builds a file list and checks for upgrades.      */
specifier|protected
name|void
name|postInitialize
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
comment|// take a snapshot of files used by the project
name|files
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
name|buildFileList
argument_list|()
argument_list|)
expr_stmt|;
name|upgradeMessages
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|checkForUpgrades
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns true if project location is not defined. For instance, when project was      * created in memory and is not tied to a file yet.      */
specifier|public
name|boolean
name|isLocationUndefined
parameter_list|()
block|{
return|return
name|getMainFile
argument_list|()
operator|==
literal|null
return|;
block|}
comment|/**      * Returns project upgrade status. "0" means project version matches the framework      * version, "-1" means project is older than the framework, "+1" means the framework      * is older than the project.      *       * @since 2.0      */
specifier|public
name|int
name|getUpgradeStatus
parameter_list|()
block|{
comment|// hardcoded pending switch to project2 framework
return|return
literal|0
return|;
block|}
comment|/**      * Returns a list of upgrade messages.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getUpgradeMessages
parameter_list|()
block|{
return|return
name|upgradeMessages
return|;
block|}
comment|/**      * Returns true is project has renamed files. This is useful when converting from      * older versions of the modeler projects.      */
specifier|public
name|boolean
name|hasRenamedFiles
parameter_list|()
block|{
if|if
condition|(
name|files
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
synchronized|synchronized
init|(
name|files
init|)
block|{
for|for
control|(
name|ProjectFile
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|isRenamed
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Creates a list of project files.      */
specifier|public
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|buildFileList
parameter_list|()
block|{
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|projectFiles
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectFile
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
name|nodes
init|=
name|treeNodes
argument_list|()
decl_stmt|;
while|while
condition|(
name|nodes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectPath
name|nodePath
init|=
operator|(
name|ProjectPath
operator|)
name|nodes
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|obj
init|=
name|nodePath
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|ProjectFile
name|f
init|=
name|projectFileForObject
argument_list|(
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|projectFiles
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|projectFiles
return|;
block|}
comment|/**      * Creates an instance of Validator for validating this project.      */
specifier|public
name|Validator
name|getValidator
parameter_list|()
block|{
return|return
operator|new
name|Validator
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Looks up and returns a file wrapper for a project object. Returns null if no file      * exists.      */
specifier|public
name|ProjectFile
name|findFile
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// to avoid full scan, a map may be a better
comment|// choice of collection here,
comment|// though normally projects have very few files...
synchronized|synchronized
init|(
name|files
init|)
block|{
for|for
control|(
name|ProjectFile
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|getObject
argument_list|()
operator|==
name|obj
condition|)
block|{
return|return
name|file
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns a canonical file built from symbolic name.      */
specifier|public
name|File
name|resolveFile
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
try|try
block|{
comment|// substitute to Windows backslashes if needed
if|if
condition|(
name|File
operator|.
name|separatorChar
operator|!=
literal|'/'
condition|)
block|{
name|symbolicName
operator|=
name|symbolicName
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|File
argument_list|(
name|projectDir
argument_list|,
name|symbolicName
argument_list|)
operator|.
name|getCanonicalFile
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// error converting path
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns a "symbolic" name of a file. Returns null if file is invalid. Symbolic name      * is a string path of a file relative to the project directory. It is built in a      * platform independent fashion.      */
specifier|public
name|String
name|resolveSymbolicName
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|symbolicName
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// accept absolute files only when
comment|// they are in the project directory
name|String
name|otherPath
init|=
name|file
operator|.
name|getCanonicalFile
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|thisPath
init|=
name|projectDir
operator|.
name|getPath
argument_list|()
decl_stmt|;
comment|// invalid absolute pathname, can't continue
if|if
condition|(
name|otherPath
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|<=
name|thisPath
operator|.
name|length
argument_list|()
operator|||
operator|!
name|otherPath
operator|.
name|startsWith
argument_list|(
name|thisPath
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|symbolicName
operator|=
name|otherPath
operator|.
name|substring
argument_list|(
name|thisPath
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
comment|// substitute Windows backslashes if needed
if|if
condition|(
operator|(
name|symbolicName
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|File
operator|.
name|separatorChar
operator|!=
literal|'/'
operator|)
condition|)
block|{
name|symbolicName
operator|=
name|symbolicName
operator|.
name|replace
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
return|return
name|symbolicName
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// error converting path
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns project directory. This is a directory where project file is located.      */
specifier|public
name|File
name|getProjectDirectory
parameter_list|()
block|{
return|return
name|projectDir
return|;
block|}
specifier|public
name|void
name|setProjectDirectory
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
name|this
operator|.
name|projectDir
operator|=
name|dir
expr_stmt|;
block|}
comment|/**      * Returns a canonical form of a main file associated with this project.      */
specifier|public
name|File
name|getMainFile
parameter_list|()
block|{
if|if
condition|(
name|projectDir
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ProjectFile
name|f
init|=
name|projectFileForObject
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|f
operator|!=
literal|null
operator|)
condition|?
name|resolveFile
argument_list|(
name|f
operator|.
name|getLocation
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * @return An object describing failures in the loaded project.      */
specifier|public
specifier|abstract
name|ConfigStatus
name|getLoadStatus
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|ProjectFile
name|projectFileForObject
parameter_list|(
name|Object
name|obj
parameter_list|)
function_decl|;
comment|/**      * Returns a list of first-level children of the project.      */
specifier|public
specifier|abstract
name|List
name|getChildren
parameter_list|()
function_decl|;
comment|/**      * Determines whether the project needs to be upgraded. Populates internal list of      * upgrade messages with discovered information.      */
specifier|public
specifier|abstract
name|void
name|checkForUpgrades
parameter_list|()
function_decl|;
comment|/**      * Returns an Iterator over project tree of objects.      */
specifier|public
name|Iterator
name|treeNodes
parameter_list|()
block|{
return|return
name|FlatProjectView
operator|.
name|getInstance
argument_list|()
operator|.
name|flattenProjectTree
argument_list|(
name|this
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
specifier|abstract
name|void
name|upgrade
parameter_list|()
throws|throws
name|ProjectException
function_decl|;
comment|/**      * Saves project. All currently existing files are updated, without checking for      * modifications. New files are created as needed, unused files are deleted.      */
specifier|public
name|void
name|save
parameter_list|()
throws|throws
name|ProjectException
block|{
comment|// sanity check
if|if
condition|(
name|isLocationUndefined
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Project location is undefined."
argument_list|)
throw|;
block|}
comment|// 1. Traverse project tree to find file wrappers that require update.
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|filesToSave
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectFile
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|wrappedObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|prepareSave
argument_list|(
name|filesToSave
argument_list|,
name|wrappedObjects
argument_list|)
expr_stmt|;
comment|// 2. Try saving individual file wrappers
name|processSave
argument_list|(
name|filesToSave
argument_list|)
expr_stmt|;
comment|// 3. Commit changes
name|List
argument_list|<
name|File
argument_list|>
name|savedFiles
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProjectFile
name|file
range|:
name|filesToSave
control|)
block|{
name|savedFiles
operator|.
name|add
argument_list|(
name|file
operator|.
name|saveCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// 4. Take care of deleted
name|processDelete
argument_list|(
name|wrappedObjects
argument_list|,
name|savedFiles
argument_list|)
expr_stmt|;
comment|// 5. Refresh file list
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|freshList
init|=
name|buildFileList
argument_list|()
decl_stmt|;
for|for
control|(
name|ProjectFile
name|file
range|:
name|freshList
control|)
block|{
name|file
operator|.
name|synchronizeLocation
argument_list|()
expr_stmt|;
block|}
name|files
operator|=
name|freshList
expr_stmt|;
synchronized|synchronized
init|(
name|upgradeMessages
init|)
block|{
name|upgradeMessages
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// update state
name|setModified
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|prepareSave
parameter_list|(
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|filesToSave
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|wrappedObjects
parameter_list|)
throws|throws
name|ProjectException
block|{
name|Iterator
name|nodes
init|=
name|treeNodes
argument_list|()
decl_stmt|;
while|while
condition|(
name|nodes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProjectPath
name|nodePath
init|=
operator|(
name|ProjectPath
operator|)
name|nodes
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|obj
init|=
name|nodePath
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|ProjectFile
name|existingFile
init|=
name|findFile
argument_list|(
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingFile
operator|==
literal|null
condition|)
block|{
comment|// check if project node can have a file
name|ProjectFile
name|newFile
init|=
name|projectFileForObject
argument_list|(
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|newFile
operator|!=
literal|null
condition|)
block|{
name|filesToSave
operator|.
name|add
argument_list|(
name|newFile
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|existingFile
operator|.
name|canHandleObject
argument_list|()
condition|)
block|{
name|wrappedObjects
operator|.
name|add
argument_list|(
name|existingFile
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|filesToSave
operator|.
name|add
argument_list|(
name|existingFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Saves a list of modified files to temporary files.      */
specifier|protected
name|void
name|processSave
parameter_list|(
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|modifiedFiles
parameter_list|)
throws|throws
name|ProjectException
block|{
comment|// notify that files will be saved
for|for
control|(
name|ProjectFile
name|file
range|:
name|modifiedFiles
control|)
block|{
name|file
operator|.
name|willSave
argument_list|()
expr_stmt|;
block|}
try|try
block|{
for|for
control|(
name|ProjectFile
name|file
range|:
name|modifiedFiles
control|)
block|{
name|file
operator|.
name|saveTemp
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
for|for
control|(
name|ProjectFile
name|file
range|:
name|modifiedFiles
control|)
block|{
name|file
operator|.
name|saveUndo
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Project save failed and was cancelled."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|processDelete
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|existingObjects
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|savedFiles
parameter_list|)
block|{
comment|// check for deleted
synchronized|synchronized
init|(
name|files
init|)
block|{
for|for
control|(
name|ProjectFile
name|f
range|:
name|files
control|)
block|{
name|File
name|file
init|=
name|f
operator|.
name|resolveOldFile
argument_list|()
decl_stmt|;
comment|// this check is needed, since a file can reuse the name
comment|// of a recently deleted file, and we don't want to delete
comment|// new file by mistake
if|if
condition|(
name|file
operator|==
literal|null
operator|||
name|savedFiles
operator|.
name|contains
argument_list|(
name|file
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|boolean
name|delete
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isRenamed
argument_list|()
condition|)
block|{
name|delete
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|f
operator|.
name|getObject
argument_list|()
operator|==
literal|null
condition|)
block|{
name|delete
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|existingObjects
operator|.
name|contains
argument_list|(
name|f
operator|.
name|getObject
argument_list|()
argument_list|)
condition|)
block|{
name|delete
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|f
operator|.
name|canHandleObject
argument_list|()
condition|)
block|{
comment|// this happens too - node can start using JNDI for instance
name|delete
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|delete
condition|)
block|{
name|deleteFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|boolean
name|deleteFile
parameter_list|(
name|File
name|f
parameter_list|)
block|{
return|return
operator|(
name|f
operator|.
name|exists
argument_list|()
operator|)
condition|?
name|f
operator|.
name|delete
argument_list|()
else|:
literal|true
return|;
block|}
comment|/**      * Returns<code>true</code> if the project is modified.      */
specifier|public
name|boolean
name|isModified
parameter_list|()
block|{
return|return
name|modified
return|;
block|}
comment|/**      * Updates "modified" state of the project.      */
specifier|public
name|void
name|setModified
parameter_list|(
name|boolean
name|modified
parameter_list|)
block|{
name|this
operator|.
name|modified
operator|=
name|modified
expr_stmt|;
block|}
block|}
end_class

end_unit

