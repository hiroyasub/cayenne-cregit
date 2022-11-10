begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|configuration
operator|.
name|ConfigurationNameMapper
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
name|configuration
operator|.
name|ConfigurationNode
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|di
operator|.
name|Inject
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
name|extension
operator|.
name|ProjectExtension
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
name|extension
operator|.
name|SaverDelegate
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
name|resource
operator|.
name|Resource
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
name|resource
operator|.
name|URLResource
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
name|XMLEncoder
import|;
end_import

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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Collection
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

begin_comment
comment|/**  * A ProjectSaver saving project configuration to the file system.  *  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|FileProjectSaver
implements|implements
name|ProjectSaver
block|{
annotation|@
name|Inject
specifier|protected
name|ConfigurationNameMapper
name|nameMapper
decl_stmt|;
specifier|protected
name|ConfigurationNodeVisitor
argument_list|<
name|Resource
argument_list|>
name|resourceGetter
decl_stmt|;
specifier|protected
name|ConfigurationNodeVisitor
argument_list|<
name|Collection
argument_list|<
name|ConfigurationNode
argument_list|>
argument_list|>
name|saveableNodesGetter
decl_stmt|;
specifier|protected
name|String
name|fileEncoding
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|ProjectExtension
argument_list|>
name|extensions
decl_stmt|;
specifier|protected
name|SaverDelegate
name|delegate
decl_stmt|;
specifier|public
name|FileProjectSaver
parameter_list|(
annotation|@
name|Inject
name|List
argument_list|<
name|ProjectExtension
argument_list|>
name|extensions
parameter_list|)
block|{
name|resourceGetter
operator|=
operator|new
name|ConfigurationSourceGetter
argument_list|()
expr_stmt|;
name|saveableNodesGetter
operator|=
operator|new
name|SaveableNodesGetter
argument_list|()
expr_stmt|;
comment|// this is not configurable yet... probably doesn't have to be
name|fileEncoding
operator|=
literal|"UTF-8"
expr_stmt|;
name|this
operator|.
name|extensions
operator|=
name|extensions
expr_stmt|;
name|Collection
argument_list|<
name|SaverDelegate
argument_list|>
name|delegates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|extensions
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProjectExtension
name|extension
range|:
name|extensions
control|)
block|{
name|delegates
operator|.
name|add
argument_list|(
name|extension
operator|.
name|createSaverDelegate
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|delegate
operator|=
operator|new
name|CompoundSaverDelegate
argument_list|(
name|delegates
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getSupportedVersion
parameter_list|()
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|Project
operator|.
name|VERSION
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|save
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|save
argument_list|(
name|project
argument_list|,
name|project
operator|.
name|getConfigurationResource
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveAs
parameter_list|(
name|Project
name|project
parameter_list|,
name|Resource
name|baseDirectory
parameter_list|)
block|{
if|if
condition|(
name|baseDirectory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null 'baseDirectory'"
argument_list|)
throw|;
block|}
name|save
argument_list|(
name|project
argument_list|,
name|baseDirectory
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|void
name|save
parameter_list|(
name|Project
name|project
parameter_list|,
name|Resource
name|baseResource
parameter_list|,
name|boolean
name|deleteOldResources
parameter_list|)
block|{
name|Collection
argument_list|<
name|ConfigurationNode
argument_list|>
name|nodes
init|=
name|project
operator|.
name|getRootNode
argument_list|()
operator|.
name|acceptVisitor
argument_list|(
name|saveableNodesGetter
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|SaveUnit
argument_list|>
name|units
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|nodes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|delegate
operator|.
name|setBaseDirectory
argument_list|(
name|baseResource
argument_list|)
expr_stmt|;
for|for
control|(
name|ConfigurationNode
name|node
range|:
name|nodes
control|)
block|{
name|String
name|targetLocation
init|=
name|nameMapper
operator|.
name|configurationLocation
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|Resource
name|targetResource
init|=
name|baseResource
operator|.
name|getRelativeResource
argument_list|(
name|targetLocation
argument_list|)
decl_stmt|;
name|units
operator|.
name|add
argument_list|(
name|createSaveUnit
argument_list|(
name|node
argument_list|,
name|targetResource
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ProjectExtension
name|extension
range|:
name|extensions
control|)
block|{
name|ConfigurationNodeVisitor
argument_list|<
name|String
argument_list|>
name|namingDelegate
init|=
name|extension
operator|.
name|createNamingDelegate
argument_list|()
decl_stmt|;
name|SaverDelegate
name|unitSaverDelegate
init|=
name|extension
operator|.
name|createSaverDelegate
argument_list|()
decl_stmt|;
name|String
name|fileName
init|=
name|node
operator|.
name|acceptVisitor
argument_list|(
name|namingDelegate
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileName
operator|!=
literal|null
condition|)
block|{
comment|// not null means that this should go to a separate file
name|targetResource
operator|=
name|baseResource
operator|.
name|getRelativeResource
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|units
operator|.
name|add
argument_list|(
name|createSaveUnit
argument_list|(
name|node
argument_list|,
name|targetResource
argument_list|,
name|unitSaverDelegate
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|checkAccess
argument_list|(
name|units
argument_list|)
expr_stmt|;
try|try
block|{
name|saveToTempFiles
argument_list|(
name|units
argument_list|)
expr_stmt|;
name|saveCommit
argument_list|(
name|units
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|clearTempFiles
argument_list|(
name|units
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|deleteOldResources
condition|)
block|{
name|clearRenamedFiles
argument_list|(
name|units
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|URL
argument_list|>
name|unusedResources
init|=
name|project
operator|.
name|getUnusedResources
argument_list|()
decl_stmt|;
for|for
control|(
name|SaveUnit
name|unit
range|:
name|units
control|)
block|{
name|unusedResources
operator|.
name|remove
argument_list|(
name|unit
operator|.
name|sourceConfiguration
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|deleteUnusedFiles
argument_list|(
name|unusedResources
argument_list|)
expr_stmt|;
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
name|CayenneRuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
comment|// I guess we should reset projects state regardless of the value of
comment|// 'deleteOldResources'
name|project
operator|.
name|getUnusedResources
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|SaveUnit
name|createSaveUnit
parameter_list|(
name|ConfigurationNode
name|node
parameter_list|,
name|Resource
name|targetResource
parameter_list|,
name|SaverDelegate
name|delegate
parameter_list|)
block|{
name|SaveUnit
name|unit
init|=
operator|new
name|SaveUnit
argument_list|()
decl_stmt|;
name|unit
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|unit
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|unit
operator|.
name|sourceConfiguration
operator|=
name|node
operator|.
name|acceptVisitor
argument_list|(
name|resourceGetter
argument_list|)
expr_stmt|;
if|if
condition|(
name|unit
operator|.
name|sourceConfiguration
operator|==
literal|null
condition|)
block|{
name|unit
operator|.
name|sourceConfiguration
operator|=
name|targetResource
expr_stmt|;
block|}
comment|// attempt to convert targetResource to a File... if that fails,
comment|// FileProjectSaver is not appropriate for handling a given project..
name|URL
name|targetUrl
init|=
name|targetResource
operator|.
name|getURL
argument_list|()
decl_stmt|;
try|try
block|{
name|unit
operator|.
name|targetFile
operator|=
name|Util
operator|.
name|toFile
argument_list|(
name|targetUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't save configuration to the following location: '%s'. "
operator|+
literal|"Is this a valid file location?. (%s)"
argument_list|,
name|e
argument_list|,
name|targetUrl
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|unit
return|;
block|}
name|void
name|checkAccess
parameter_list|(
name|Collection
argument_list|<
name|SaveUnit
argument_list|>
name|units
parameter_list|)
block|{
for|for
control|(
name|SaveUnit
name|unit
range|:
name|units
control|)
block|{
name|File
name|targetFile
init|=
name|unit
operator|.
name|targetFile
decl_stmt|;
name|File
name|parent
init|=
name|targetFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|parent
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|parent
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error creating directory tree for '%s'"
argument_list|,
name|parent
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|targetFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Target file '%s' is a directory"
argument_list|,
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|targetFile
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|targetFile
operator|.
name|canWrite
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't write to file '%s'"
argument_list|,
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
name|void
name|saveToTempFiles
parameter_list|(
name|Collection
argument_list|<
name|SaveUnit
argument_list|>
name|units
parameter_list|)
block|{
for|for
control|(
name|SaveUnit
name|unit
range|:
name|units
control|)
block|{
name|String
name|name
init|=
name|unit
operator|.
name|targetFile
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|<
literal|3
condition|)
block|{
name|name
operator|=
literal|"cayenne-project"
expr_stmt|;
block|}
name|File
name|parent
init|=
name|unit
operator|.
name|targetFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
try|try
block|{
name|unit
operator|.
name|targetTempFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|parent
argument_list|)
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
name|CayenneRuntimeException
argument_list|(
literal|"Error creating temp file (%s)"
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|unit
operator|.
name|targetTempFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|unit
operator|.
name|targetTempFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|PrintWriter
name|printWriter
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|unit
operator|.
name|targetTempFile
argument_list|)
argument_list|,
name|fileEncoding
argument_list|)
argument_list|)
init|)
block|{
name|saveToTempFile
argument_list|(
name|unit
argument_list|,
name|printWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported encoding '%s' (%s)"
argument_list|,
name|e
argument_list|,
name|fileEncoding
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"File not found '%s' (%s)"
argument_list|,
name|e
argument_list|,
name|unit
operator|.
name|targetTempFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
name|void
name|saveToTempFile
parameter_list|(
name|SaveUnit
name|unit
parameter_list|,
name|PrintWriter
name|printWriter
parameter_list|)
block|{
name|ConfigurationNodeVisitor
argument_list|<
name|?
argument_list|>
name|visitor
decl_stmt|;
if|if
condition|(
name|unit
operator|.
name|delegate
operator|==
literal|null
condition|)
block|{
name|visitor
operator|=
operator|new
name|ConfigurationSaver
argument_list|(
name|printWriter
argument_list|,
name|getSupportedVersion
argument_list|()
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|XMLEncoder
name|encoder
init|=
operator|new
name|XMLEncoder
argument_list|(
name|printWriter
argument_list|,
literal|"\t"
argument_list|,
name|getSupportedVersion
argument_list|()
argument_list|)
decl_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
argument_list|)
expr_stmt|;
name|unit
operator|.
name|delegate
operator|.
name|setXMLEncoder
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|visitor
operator|=
name|unit
operator|.
name|delegate
expr_stmt|;
block|}
name|unit
operator|.
name|node
operator|.
name|acceptVisitor
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
name|void
name|saveCommit
parameter_list|(
name|Collection
argument_list|<
name|SaveUnit
argument_list|>
name|units
parameter_list|)
block|{
for|for
control|(
name|SaveUnit
name|unit
range|:
name|units
control|)
block|{
name|File
name|targetFile
init|=
name|unit
operator|.
name|targetFile
decl_stmt|;
comment|// Per CAY-2119, this is an ugly hack to force Windows to unlock the file that was previously locked by
comment|// our process. Without it, the delete operation downstream would fail
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
if|if
condition|(
name|targetFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|targetFile
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to remove old master file '%s'"
argument_list|,
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|File
name|tempFile
init|=
name|unit
operator|.
name|targetTempFile
decl_stmt|;
if|if
condition|(
operator|!
name|tempFile
operator|.
name|renameTo
argument_list|(
name|targetFile
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to move '%s' to '%s'"
argument_list|,
name|tempFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|unit
operator|.
name|targetTempFile
operator|=
literal|null
expr_stmt|;
try|try
block|{
if|if
condition|(
name|unit
operator|.
name|delegate
operator|==
literal|null
condition|)
block|{
name|URLResource
name|targetUrlResource
init|=
operator|new
name|URLResource
argument_list|(
name|targetFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
decl_stmt|;
name|unit
operator|.
name|node
operator|.
name|acceptVisitor
argument_list|(
operator|new
name|ConfigurationSourceSetter
argument_list|(
name|targetUrlResource
argument_list|,
name|nameMapper
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Malformed URL for file '%s'"
argument_list|,
name|e
argument_list|,
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|clearTempFiles
parameter_list|(
name|Collection
argument_list|<
name|SaveUnit
argument_list|>
name|units
parameter_list|)
block|{
for|for
control|(
name|SaveUnit
name|unit
range|:
name|units
control|)
block|{
if|if
condition|(
name|unit
operator|.
name|targetTempFile
operator|!=
literal|null
operator|&&
name|unit
operator|.
name|targetTempFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|unit
operator|.
name|targetTempFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|unit
operator|.
name|targetTempFile
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|clearRenamedFiles
parameter_list|(
name|Collection
argument_list|<
name|SaveUnit
argument_list|>
name|units
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|SaveUnit
name|unit
range|:
name|units
control|)
block|{
if|if
condition|(
name|unit
operator|.
name|sourceConfiguration
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|URL
name|sourceUrl
init|=
name|unit
operator|.
name|sourceConfiguration
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|File
name|sourceFile
decl_stmt|;
try|try
block|{
name|sourceFile
operator|=
name|Util
operator|.
name|toFile
argument_list|(
name|sourceUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// ignore non-file configurations...
continue|continue;
block|}
if|if
condition|(
operator|!
name|sourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
continue|continue;
block|}
comment|// compare against ALL unit target files, not just the current
comment|// unit... if the
comment|// target matches, skip this file
name|boolean
name|isTarget
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SaveUnit
name|xunit
range|:
name|units
control|)
block|{
if|if
condition|(
name|isFilesEquals
argument_list|(
name|sourceFile
argument_list|,
name|xunit
operator|.
name|targetFile
argument_list|)
condition|)
block|{
name|isTarget
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isTarget
condition|)
block|{
if|if
condition|(
operator|!
name|sourceFile
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Could not delete file '%s'"
argument_list|,
name|sourceFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isFilesEquals
parameter_list|(
name|File
name|firstFile
parameter_list|,
name|File
name|secondFile
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|isFirstFileExists
init|=
name|firstFile
operator|.
name|exists
argument_list|()
decl_stmt|;
name|boolean
name|isSecondFileExists
init|=
name|secondFile
operator|.
name|exists
argument_list|()
decl_stmt|;
name|String
name|firstFilePath
init|=
name|firstFile
operator|.
name|getCanonicalPath
argument_list|()
decl_stmt|;
name|String
name|secondFilePath
init|=
name|secondFile
operator|.
name|getCanonicalPath
argument_list|()
decl_stmt|;
return|return
name|isFirstFileExists
operator|&&
name|isSecondFileExists
operator|&&
name|firstFilePath
operator|.
name|equals
argument_list|(
name|secondFilePath
argument_list|)
return|;
block|}
specifier|private
name|void
name|deleteUnusedFiles
parameter_list|(
name|Collection
argument_list|<
name|URL
argument_list|>
name|unusedResources
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|URL
name|unusedResource
range|:
name|unusedResources
control|)
block|{
name|File
name|unusedFile
decl_stmt|;
try|try
block|{
name|unusedFile
operator|=
name|Util
operator|.
name|toFile
argument_list|(
name|unusedResource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// ignore non-file configurations...
continue|continue;
block|}
if|if
condition|(
operator|!
name|unusedFile
operator|.
name|exists
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|unusedFile
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Could not delete file '%s'"
argument_list|,
name|unusedFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|static
class|class
name|SaveUnit
block|{
specifier|private
name|ConfigurationNode
name|node
decl_stmt|;
specifier|private
name|SaverDelegate
name|delegate
decl_stmt|;
comment|// source can be an abstract resource, but target is always a file...
specifier|private
name|Resource
name|sourceConfiguration
decl_stmt|;
specifier|private
name|File
name|targetFile
decl_stmt|;
specifier|private
name|File
name|targetTempFile
decl_stmt|;
block|}
block|}
end_class

end_unit

