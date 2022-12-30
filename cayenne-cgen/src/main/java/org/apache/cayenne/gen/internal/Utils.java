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
name|gen
operator|.
name|internal
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
name|map
operator|.
name|DataMap
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Internally used utilities related to cgen.  *  * @since 5.0  */
end_comment

begin_class
specifier|public
class|class
name|Utils
block|{
specifier|public
specifier|static
name|Optional
argument_list|<
name|String
argument_list|>
name|getMavenSrcPathForPath
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
return|return
name|getMavenSrcPathForPath
argument_list|(
name|path
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @param path to check      * @return path in the maven src layout or {@code Optional.empty()} if we are not inside maven project structure      */
specifier|public
specifier|static
name|Optional
argument_list|<
name|String
argument_list|>
name|getMavenSrcPathForPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// check if we are in src/test/resources
name|String
name|testDirPath
init|=
name|checkDefaultMavenResourceDir
argument_list|(
name|path
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
if|if
condition|(
name|testDirPath
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|testDirPath
argument_list|)
return|;
block|}
comment|// check if we are in src/main/resources
name|String
name|mainDirPath
init|=
name|checkDefaultMavenResourceDir
argument_list|(
name|path
argument_list|,
literal|"main"
argument_list|)
decl_stmt|;
if|if
condition|(
name|mainDirPath
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|mainDirPath
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Path
name|getRootPathForDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
if|if
condition|(
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to create path from the unsaved DataMap"
argument_list|)
throw|;
block|}
name|Path
name|resourcePath
decl_stmt|;
try|try
block|{
name|resourcePath
operator|=
name|Path
operator|.
name|of
argument_list|(
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to create path from the DataMap source location"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|resourcePath
argument_list|)
condition|)
block|{
name|resourcePath
operator|=
name|resourcePath
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
name|resourcePath
return|;
block|}
specifier|private
specifier|static
name|String
name|checkDefaultMavenResourceDir
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|dirType
parameter_list|)
block|{
name|String
name|resourcePath
init|=
name|buildFilePath
argument_list|(
literal|"src"
argument_list|,
name|dirType
argument_list|,
literal|"resources"
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
name|resourcePath
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
operator|+
name|buildFilePath
argument_list|(
literal|"src"
argument_list|,
name|dirType
argument_list|,
literal|"java"
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|buildFilePath
parameter_list|(
name|String
modifier|...
name|pathElements
parameter_list|)
block|{
return|return
name|String
operator|.
name|join
argument_list|(
name|File
operator|.
name|separator
argument_list|,
name|pathElements
argument_list|)
return|;
block|}
block|}
end_class

end_unit

