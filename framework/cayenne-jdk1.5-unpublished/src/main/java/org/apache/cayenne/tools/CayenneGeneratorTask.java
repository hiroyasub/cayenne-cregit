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
name|tools
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|gen
operator|.
name|ClassGenerator
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
name|gen
operator|.
name|DefaultClassGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|foundrylogic
operator|.
name|vpp
operator|.
name|VPPConfig
import|;
end_import

begin_comment
comment|/**  * An Ant task to perform class generation based on CayenneDataMap.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|CayenneGeneratorTask
extends|extends
name|CayenneTask
block|{
specifier|protected
name|String
name|includeEntitiesPattern
decl_stmt|;
specifier|protected
name|String
name|excludeEntitiesPattern
decl_stmt|;
specifier|protected
name|VPPConfig
name|vppConfig
decl_stmt|;
specifier|protected
name|File
name|map
decl_stmt|;
specifier|protected
name|File
name|additionalMaps
index|[]
decl_stmt|;
specifier|protected
name|DefaultClassGenerator
name|generator
decl_stmt|;
specifier|protected
name|CayenneGeneratorUtil
name|generatorUtil
decl_stmt|;
specifier|public
name|CayenneGeneratorTask
parameter_list|()
block|{
name|generator
operator|=
name|createGenerator
argument_list|()
expr_stmt|;
name|generatorUtil
operator|=
operator|new
name|CayenneGeneratorUtil
argument_list|()
expr_stmt|;
block|}
comment|/**      * Factory method to create internal class generator. Called from constructor.      */
specifier|protected
name|DefaultClassGenerator
name|createGenerator
parameter_list|()
block|{
name|AntClassGenerator
name|gen
init|=
operator|new
name|AntClassGenerator
argument_list|()
decl_stmt|;
name|gen
operator|.
name|setParentTask
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|gen
return|;
block|}
comment|/**      * Executes the task. It will be called by ant framework.      */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|validateAttributes
argument_list|()
expr_stmt|;
comment|// Take care of setting up VPP for the generator.
if|if
condition|(
operator|!
name|ClassGenerator
operator|.
name|VERSION_1_1
operator|.
name|equals
argument_list|(
name|generator
operator|.
name|getVersionString
argument_list|()
argument_list|)
condition|)
block|{
name|initializeVppConfig
argument_list|()
expr_stmt|;
name|generator
operator|.
name|setVppConfig
argument_list|(
name|vppConfig
argument_list|)
expr_stmt|;
block|}
comment|// Initialize the util generator state.
name|generatorUtil
operator|.
name|setAdditionalMaps
argument_list|(
name|additionalMaps
argument_list|)
expr_stmt|;
name|generatorUtil
operator|.
name|setExcludeEntitiesPattern
argument_list|(
name|excludeEntitiesPattern
argument_list|)
expr_stmt|;
name|generatorUtil
operator|.
name|setGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
name|generatorUtil
operator|.
name|setIncludeEntitiesPattern
argument_list|(
name|includeEntitiesPattern
argument_list|)
expr_stmt|;
name|generatorUtil
operator|.
name|setLogger
argument_list|(
operator|new
name|AntTaskLogger
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|generatorUtil
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
try|try
block|{
name|generatorUtil
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Validates attributes that are not related to internal DefaultClassGenerator.      * Throws BuildException if attributes are invalid.      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|map
operator|==
literal|null
operator|&&
name|this
operator|.
name|getProject
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"either 'map' or 'project' is required."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets the map.      *       * @param map The map to set      */
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
comment|/**      * Sets the additional DataMaps.      *       * @param additionalMapsPath The additional DataMaps to set      */
specifier|public
name|void
name|setAdditionalMaps
parameter_list|(
name|Path
name|additionalMapsPath
parameter_list|)
block|{
name|String
name|additionalMapFilenames
index|[]
init|=
name|additionalMapsPath
operator|.
name|list
argument_list|()
decl_stmt|;
name|this
operator|.
name|additionalMaps
operator|=
operator|new
name|File
index|[
name|additionalMapFilenames
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|additionalMapFilenames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|additionalMaps
index|[
name|i
index|]
operator|=
operator|new
name|File
argument_list|(
name|additionalMapFilenames
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the destDir.      */
specifier|public
name|void
name|setDestDir
parameter_list|(
name|File
name|destDir
parameter_list|)
block|{
name|generator
operator|.
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>overwrite</code> property.      */
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|generator
operator|.
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>makepairs</code> property.      */
specifier|public
name|void
name|setMakepairs
parameter_list|(
name|boolean
name|makepairs
parameter_list|)
block|{
name|generator
operator|.
name|setMakePairs
argument_list|(
name|makepairs
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>template</code> property.      */
specifier|public
name|void
name|setTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|generator
operator|.
name|setTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>supertemplate</code> property.      */
specifier|public
name|void
name|setSupertemplate
parameter_list|(
name|String
name|supertemplate
parameter_list|)
block|{
name|generator
operator|.
name|setSuperTemplate
argument_list|(
name|supertemplate
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>usepkgpath</code> property.      */
specifier|public
name|void
name|setUsepkgpath
parameter_list|(
name|boolean
name|usepkgpath
parameter_list|)
block|{
name|generator
operator|.
name|setUsePkgPath
argument_list|(
name|usepkgpath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>superpkg</code> property.      */
specifier|public
name|void
name|setSuperpkg
parameter_list|(
name|String
name|superpkg
parameter_list|)
block|{
name|generator
operator|.
name|setSuperPkg
argument_list|(
name|superpkg
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>client</code> property.      */
specifier|public
name|void
name|setClient
parameter_list|(
name|boolean
name|client
parameter_list|)
block|{
name|generator
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>version</code> property.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|versionString
parameter_list|)
block|{
try|try
block|{
name|generator
operator|.
name|setVersionString
argument_list|(
name|versionString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets<code>encoding</code> property that allows to generate files using      * non-default encoding.      */
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|generator
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>excludeEntitiesPattern</code> property.      */
specifier|public
name|void
name|setExcludeEntities
parameter_list|(
name|String
name|excludeEntitiesPattern
parameter_list|)
block|{
name|this
operator|.
name|excludeEntitiesPattern
operator|=
name|excludeEntitiesPattern
expr_stmt|;
block|}
comment|/**      * Sets<code>includeEntitiesPattern</code> property.      */
specifier|public
name|void
name|setIncludeEntities
parameter_list|(
name|String
name|includeEntitiesPattern
parameter_list|)
block|{
name|this
operator|.
name|includeEntitiesPattern
operator|=
name|includeEntitiesPattern
expr_stmt|;
block|}
comment|/**      * Sets<code>outputPattern</code> property.      */
specifier|public
name|void
name|setOutputPattern
parameter_list|(
name|String
name|outputPattern
parameter_list|)
block|{
name|generator
operator|.
name|setOutputPattern
argument_list|(
name|outputPattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets<code>mode</code> property.      */
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|generator
operator|.
name|setMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Provides a<code>VPPConfig</code> object to configure. (Written with      * createConfig() instead of addConfig() to avoid run-time dependency on VPP).      */
specifier|public
name|Object
name|createConfig
parameter_list|()
block|{
name|this
operator|.
name|vppConfig
operator|=
operator|new
name|VPPConfig
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|vppConfig
return|;
block|}
comment|/**      * If no VppConfig element specified, use the default one.      */
specifier|private
name|void
name|initializeVppConfig
parameter_list|()
block|{
if|if
condition|(
name|vppConfig
operator|==
literal|null
condition|)
block|{
name|vppConfig
operator|=
name|VPPConfig
operator|.
name|getDefaultConfig
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

