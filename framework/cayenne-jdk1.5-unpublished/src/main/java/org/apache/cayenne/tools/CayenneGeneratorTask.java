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
name|gen
operator|.
name|ClassGenerationAction
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
name|ClassGenerationAction1_1
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
name|ClassGeneratorMode
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
name|ClientClassGenerationAction
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
name|cayenne
operator|.
name|map
operator|.
name|ObjEntity
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
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
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
name|boolean
name|client
decl_stmt|;
specifier|protected
name|File
name|destDir
decl_stmt|;
specifier|protected
name|String
name|encoding
decl_stmt|;
specifier|protected
name|boolean
name|makepairs
decl_stmt|;
specifier|protected
name|String
name|mode
decl_stmt|;
specifier|protected
name|String
name|outputPattern
decl_stmt|;
specifier|protected
name|boolean
name|overwrite
decl_stmt|;
specifier|protected
name|String
name|superpkg
decl_stmt|;
specifier|protected
name|String
name|supertemplate
decl_stmt|;
specifier|protected
name|String
name|template
decl_stmt|;
specifier|protected
name|boolean
name|usepkgpath
decl_stmt|;
comment|/**      * @deprecated since 3.0      */
specifier|protected
name|String
name|version
decl_stmt|;
specifier|public
name|CayenneGeneratorTask
parameter_list|()
block|{
name|this
operator|.
name|makepairs
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|ClassGeneratorMode
operator|.
name|entity
operator|.
name|name
argument_list|()
expr_stmt|;
name|this
operator|.
name|outputPattern
operator|=
literal|"*.java"
expr_stmt|;
name|this
operator|.
name|usepkgpath
operator|=
literal|true
expr_stmt|;
block|}
specifier|protected
name|VelocityContext
name|getVppContext
parameter_list|()
block|{
name|initializeVppConfig
argument_list|()
expr_stmt|;
return|return
name|vppConfig
operator|.
name|getVelocityContext
argument_list|()
return|;
block|}
specifier|protected
name|ClassGenerationAction
name|createGeneratorAction
parameter_list|()
block|{
name|ClassGenerationAction
name|action
decl_stmt|;
if|if
condition|(
name|client
condition|)
block|{
name|action
operator|=
operator|new
name|ClientClassGenerationAction
argument_list|()
expr_stmt|;
name|action
operator|.
name|setContext
argument_list|(
name|getVppContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ClassGenerator
operator|.
name|VERSION_1_1
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|action
operator|=
operator|new
name|ClassGenerationAction1_1
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|action
operator|=
operator|new
name|ClassGenerationAction
argument_list|()
expr_stmt|;
name|action
operator|.
name|setContext
argument_list|(
name|getVppContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|action
operator|.
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
name|action
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|action
operator|.
name|setMakePairs
argument_list|(
name|makepairs
argument_list|)
expr_stmt|;
name|action
operator|.
name|setMode
argument_list|(
name|ClassGeneratorMode
operator|.
name|valueOf
argument_list|(
name|mode
argument_list|)
argument_list|)
expr_stmt|;
name|action
operator|.
name|setOutputPattern
argument_list|(
name|outputPattern
argument_list|)
expr_stmt|;
name|action
operator|.
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSuperPkg
argument_list|(
name|superpkg
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSuperTemplate
argument_list|(
name|supertemplate
argument_list|)
expr_stmt|;
name|action
operator|.
name|setTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|action
operator|.
name|setUsePkgPath
argument_list|(
name|usepkgpath
argument_list|)
expr_stmt|;
return|return
name|action
return|;
block|}
comment|/**      * @deprecated since 3.0. Use {@link #createGeneratorAction()}.      */
specifier|protected
name|DefaultClassGenerator
name|createGenerator
parameter_list|()
block|{
name|DefaultClassGenerator
name|gen
init|=
operator|new
name|DefaultClassGenerator
argument_list|()
decl_stmt|;
name|gen
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setMakePairs
argument_list|(
name|makepairs
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setOutputPattern
argument_list|(
name|outputPattern
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setSuperPkg
argument_list|(
name|superpkg
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setSuperTemplate
argument_list|(
name|supertemplate
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setUsePkgPath
argument_list|(
name|usepkgpath
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setVersionString
argument_list|(
name|version
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
name|AntLogger
name|logger
init|=
operator|new
name|AntLogger
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|CayenneGeneratorMapLoaderAction
name|loadAction
init|=
operator|new
name|CayenneGeneratorMapLoaderAction
argument_list|()
decl_stmt|;
name|loadAction
operator|.
name|setMainDataMapFile
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|loadAction
operator|.
name|setAdditionalDataMapFiles
argument_list|(
name|additionalMaps
argument_list|)
expr_stmt|;
name|CayenneGeneratorEntityFilterAction
name|filterAction
init|=
operator|new
name|CayenneGeneratorEntityFilterAction
argument_list|()
decl_stmt|;
name|filterAction
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|filterAction
operator|.
name|setNameFilter
argument_list|(
operator|new
name|NamePatternMatcher
argument_list|(
name|logger
argument_list|,
name|includeEntitiesPattern
argument_list|,
name|excludeEntitiesPattern
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|ClassGenerationAction
name|generatorAction
init|=
name|createGeneratorAction
argument_list|()
decl_stmt|;
name|generatorAction
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|generatorAction
operator|.
name|setTimestamp
argument_list|(
name|map
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
name|generatorAction
operator|.
name|setDataMap
argument_list|(
name|loadAction
operator|.
name|getMainDataMap
argument_list|()
argument_list|)
expr_stmt|;
name|generatorAction
operator|.
name|setEntities
argument_list|(
operator|(
name|List
argument_list|<
name|ObjEntity
argument_list|>
operator|)
name|filterAction
operator|.
name|getFilteredEntities
argument_list|(
name|loadAction
operator|.
name|getMainDataMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|generatorAction
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
comment|/**      * Validates attributes that are not related to internal DefaultClassGenerator. Throws      * BuildException if attributes are invalid.      */
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
name|this
operator|.
name|destDir
operator|=
name|destDir
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
name|this
operator|.
name|overwrite
operator|=
name|overwrite
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
name|this
operator|.
name|makepairs
operator|=
name|makepairs
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
name|this
operator|.
name|template
operator|=
name|template
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
name|this
operator|.
name|supertemplate
operator|=
name|supertemplate
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
name|this
operator|.
name|usepkgpath
operator|=
name|usepkgpath
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
name|this
operator|.
name|superpkg
operator|=
name|superpkg
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
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
comment|/**      * Sets<code>version</code> property.      *       * @deprecated since 3.0      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
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
name|this
operator|.
name|encoding
operator|=
name|encoding
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
name|this
operator|.
name|outputPattern
operator|=
name|outputPattern
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
name|this
operator|.
name|mode
operator|=
name|mode
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

