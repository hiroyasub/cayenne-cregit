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
name|Iterator
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
name|util
operator|.
name|Util
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
name|util
operator|.
name|ZipUtil
import|;
end_import

begin_comment
comment|/**  * Performs on the fly reconfiguration of Cayenne projects.  *   * @deprecated since 3.0. {@link ProjectConfigurator} approach turned out to be not  *             usable, and is in fact rarely used (if ever). It will be removed in  *             subsequent releases.  */
end_comment

begin_class
specifier|public
class|class
name|ProjectConfigurator
block|{
specifier|protected
name|ProjectConfigInfo
name|info
decl_stmt|;
specifier|public
name|ProjectConfigurator
parameter_list|(
name|ProjectConfigInfo
name|info
parameter_list|)
block|{
name|this
operator|.
name|info
operator|=
name|info
expr_stmt|;
block|}
comment|/**      * Performs reconfiguration of the project.      *       * @throws ProjectException      */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|ProjectException
block|{
name|File
name|tmpDir
init|=
literal|null
decl_stmt|;
name|File
name|tmpDest
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// initialize default settings
if|if
condition|(
name|info
operator|.
name|getDestJar
argument_list|()
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|setDestJar
argument_list|(
name|info
operator|.
name|getSourceJar
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// sanity check
name|validate
argument_list|()
expr_stmt|;
comment|// do the processing
name|tmpDir
operator|=
name|makeTempDirectory
argument_list|()
expr_stmt|;
name|ZipUtil
operator|.
name|unzip
argument_list|(
name|info
operator|.
name|getSourceJar
argument_list|()
argument_list|,
name|tmpDir
argument_list|)
expr_stmt|;
name|reconfigureProject
argument_list|(
name|tmpDir
argument_list|)
expr_stmt|;
name|tmpDest
operator|=
name|makeTempDestJar
argument_list|()
expr_stmt|;
name|ZipUtil
operator|.
name|zip
argument_list|(
name|tmpDest
argument_list|,
name|tmpDir
argument_list|,
name|tmpDir
operator|.
name|listFiles
argument_list|()
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
comment|// finally, since everything goes well so far, rename temp file to final name
if|if
condition|(
name|info
operator|.
name|getDestJar
argument_list|()
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|info
operator|.
name|getDestJar
argument_list|()
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't delete old jar file: "
operator|+
name|info
operator|.
name|getDestJar
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|tmpDest
operator|.
name|renameTo
argument_list|(
name|info
operator|.
name|getDestJar
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error renaming: "
operator|+
name|tmpDest
operator|+
literal|" to "
operator|+
name|info
operator|.
name|getDestJar
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Error performing reconfiguration."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|tmpDir
operator|!=
literal|null
condition|)
block|{
name|cleanup
argument_list|(
name|tmpDir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tmpDest
operator|!=
literal|null
condition|)
block|{
name|tmpDest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Performs reconfiguration of the unjarred project.      *       * @param projectDir a directory where a working copy of the project is located.      */
specifier|protected
name|void
name|reconfigureProject
parameter_list|(
name|File
name|projectDir
parameter_list|)
throws|throws
name|ProjectException
block|{
name|File
name|projectFile
init|=
operator|new
name|File
argument_list|(
name|projectDir
argument_list|,
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
decl_stmt|;
comment|// process alternative project file
if|if
condition|(
name|info
operator|.
name|getAltProjectFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|copy
argument_list|(
name|info
operator|.
name|getAltProjectFile
argument_list|()
argument_list|,
name|projectFile
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Can't copy project file: "
operator|+
name|info
operator|.
name|getAltProjectFile
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// copy driver files, delete unused
name|Iterator
argument_list|<
name|DataNodeConfigInfo
argument_list|>
name|it
init|=
name|info
operator|.
name|getNodes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|needFix
init|=
name|it
operator|.
name|hasNext
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataNodeConfigInfo
name|nodeInfo
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|nodeInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|File
name|targetDriverFile
init|=
operator|new
name|File
argument_list|(
name|projectDir
argument_list|,
name|name
operator|+
name|DataNodeFile
operator|.
name|LOCATION_SUFFIX
argument_list|)
decl_stmt|;
comment|// these are the two cases when the driver file must be deleted
if|if
condition|(
name|nodeInfo
operator|.
name|getDataSource
argument_list|()
operator|!=
literal|null
operator|||
name|nodeInfo
operator|.
name|getDriverFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|targetDriverFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|targetDriverFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nodeInfo
operator|.
name|getDriverFile
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|nodeInfo
operator|.
name|getDriverFile
argument_list|()
operator|.
name|equals
argument_list|(
name|targetDriverFile
argument_list|)
condition|)
block|{
comment|// need to copy file from another location
if|if
condition|(
operator|!
name|Util
operator|.
name|copy
argument_list|(
name|nodeInfo
operator|.
name|getDriverFile
argument_list|()
argument_list|,
name|targetDriverFile
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Can't copy driver file from "
operator|+
name|nodeInfo
operator|.
name|getDriverFile
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
comment|// load project
if|if
condition|(
name|needFix
condition|)
block|{
comment|// read the project and fix data nodes
name|PartialProject
name|project
init|=
operator|new
name|PartialProject
argument_list|(
name|projectFile
argument_list|)
decl_stmt|;
name|project
operator|.
name|updateNodes
argument_list|(
name|info
operator|.
name|getNodes
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns a temporary file for the destination jar.      */
specifier|protected
name|File
name|makeTempDestJar
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|destFolder
init|=
name|info
operator|.
name|getDestJar
argument_list|()
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|destFolder
operator|!=
literal|null
operator|&&
operator|!
name|destFolder
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|destFolder
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't create directory: "
operator|+
name|destFolder
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|String
name|baseName
init|=
literal|"tmp_"
operator|+
name|info
operator|.
name|getDestJar
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// seeting upper limit on a number of tries, though normally we would expect
comment|// to succeed from the first attempt...
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|File
name|tmpFile
init|=
operator|(
name|destFolder
operator|!=
literal|null
operator|)
condition|?
operator|new
name|File
argument_list|(
name|destFolder
argument_list|,
name|baseName
operator|+
name|i
argument_list|)
else|:
operator|new
name|File
argument_list|(
name|baseName
operator|+
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmpFile
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|tmpFile
return|;
block|}
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Problems creating temporary file."
argument_list|)
throw|;
block|}
comment|/**      * Deletes a temporary directories and files created.      */
specifier|protected
name|void
name|cleanup
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
name|Util
operator|.
name|delete
argument_list|(
name|dir
operator|.
name|getPath
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a temporary directory to unjar the jar file.      *       * @return File      * @throws IOException      */
specifier|protected
name|File
name|makeTempDirectory
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|destFolder
init|=
name|info
operator|.
name|getDestJar
argument_list|()
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|destFolder
operator|!=
literal|null
operator|&&
operator|!
name|destFolder
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|destFolder
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't create directory: "
operator|+
name|destFolder
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|String
name|baseName
init|=
name|info
operator|.
name|getDestJar
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseName
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
name|baseName
operator|=
name|baseName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|baseName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
comment|// seeting upper limit on a number of tries, though normally we would expect
comment|// to succeed from the first attempt...
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|File
name|tmpDir
init|=
operator|(
name|destFolder
operator|!=
literal|null
operator|)
condition|?
operator|new
name|File
argument_list|(
name|destFolder
argument_list|,
name|baseName
operator|+
name|i
argument_list|)
else|:
operator|new
name|File
argument_list|(
name|baseName
operator|+
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmpDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|tmpDir
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't create directory: "
operator|+
name|tmpDir
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|tmpDir
return|;
block|}
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Problems creating temporary directory."
argument_list|)
throw|;
block|}
comment|/**      * Validates consistency of the reconfiguration information.      */
specifier|protected
name|void
name|validate
parameter_list|()
throws|throws
name|IOException
throws|,
name|ProjectException
block|{
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"ProjectConfig info is not set."
argument_list|)
throw|;
block|}
if|if
condition|(
name|info
operator|.
name|getSourceJar
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Source jar file is not set."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|info
operator|.
name|getSourceJar
argument_list|()
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|info
operator|.
name|getSourceJar
argument_list|()
operator|+
literal|" is not a file."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|info
operator|.
name|getSourceJar
argument_list|()
operator|.
name|canRead
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't read file: "
operator|+
name|info
operator|.
name|getSourceJar
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

