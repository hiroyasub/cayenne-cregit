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
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|dbsync
operator|.
name|filter
operator|.
name|NamePatternMatcher
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
name|dbsync
operator|.
name|reverse
operator|.
name|configuration
operator|.
name|ToolsModule
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
name|Injector
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
name|ArtifactsGenerationMode
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
name|CgenConfiguration
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
name|ClassGenerationActionFactory
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
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Maven mojo to perform class generation from data cgenConfiguration. This class is an Maven  * adapter to DefaultClassGenerator class.  *   * @since 3.0  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"cgen"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|)
specifier|public
class|class
name|CayenneGeneratorMojo
extends|extends
name|AbstractMojo
block|{
specifier|public
specifier|static
specifier|final
name|File
index|[]
name|NO_FILES
init|=
operator|new
name|File
index|[
literal|0
index|]
decl_stmt|;
comment|/** 	 * Path to additional DataMap XML files to use for class generation. 	 */
annotation|@
name|Parameter
specifier|private
name|File
name|additionalMaps
decl_stmt|;
comment|/** 	 * Whether we are generating classes for the client tier in a Remote Object 	 * Persistence application. Default is<code>false</code>. 	 */
annotation|@
name|Parameter
specifier|private
name|Boolean
name|client
decl_stmt|;
comment|/** 	 * Default destination directory for Java classes (ignoring their package names). 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.sourceDirectory}"
argument_list|)
specifier|private
name|File
name|defaultDir
decl_stmt|;
comment|/** 	 * Destination directory for Java classes (ignoring their package names). 	 */
annotation|@
name|Parameter
specifier|private
name|File
name|destDir
decl_stmt|;
comment|/** 	 * Specify generated file encoding if different from the default on current 	 * platform. Target encoding must be supported by the JVM running Maven 	 * build. Standard encodings supported by Java on all platforms are 	 * US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. See Sun Java 	 * Docs for java.nio.charset.Charset for more information. 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/** 	 * Entities (expressed as a perl5 regex) to exclude from template 	 * generation. (Default is to include all entities in the DataMap). 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|excludeEntities
decl_stmt|;
comment|/** 	 * Entities (expressed as a perl5 regex) to include in template generation. 	 * (Default is to include all entities in the DataMap). 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|includeEntities
decl_stmt|;
comment|/** 	 * @since 4.1 	 * Embeddables (expressed as a perl5 regex) to exclude from template 	 * generation. (Default is to include all embeddables in the DataMap). 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|excludeEmbeddables
decl_stmt|;
comment|/** 	 * If set to<code>true</code>, will generate subclass/superclass pairs, 	 * with all generated code included in superclass (default is 	 *<code>true</code>). 	 */
annotation|@
name|Parameter
specifier|private
name|Boolean
name|makePairs
decl_stmt|;
comment|/** 	 * DataMap XML file to use as a base for class generation. 	 */
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|private
name|File
name|map
decl_stmt|;
comment|/** 	 * Specifies generator iteration target.&quot;entity&quot; performs one 	 * iteration for each selected entity.&quot;datamap&quot; performs one 	 * iteration per datamap (This is always one iteration since cgen currently 	 * supports specifying one-and-only-one datamap). (Default is&quot;entity&quot;) 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|mode
decl_stmt|;
comment|/** 	 * Name of file for generated output. (Default is&quot;*.java&quot;) 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|outputPattern
decl_stmt|;
comment|/** 	 * If set to<code>true</code>, will overwrite older versions of generated 	 * classes. Ignored unless makepairs is set to<code>false</code>. 	 */
annotation|@
name|Parameter
specifier|private
name|Boolean
name|overwrite
decl_stmt|;
comment|/** 	 * Java package name of generated superclasses. Ignored unless 	 *<code>makepairs</code> set to<code>true</code>. If omitted, each 	 * superclass will be assigned the same package as subclass. Note that 	 * having superclass in a different package would only make sense when 	 *<code>usepkgpath</code> is set to<code>true</code>. Otherwise classes 	 * from different packages will end up in the same directory. 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|superPkg
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Entity superclass generation. 	 * Ignored unless<code>makepairs</code> set to<code>true</code>. If 	 * omitted, default template is used. 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|superTemplate
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Entity class generation. If 	 * omitted, default template is used. 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|template
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Embeddable superclass generation. 	 * Ignored unless<code>makepairs</code> set to<code>true</code>. If 	 * omitted, default template is used. 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|embeddableSuperTemplate
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Embeddable class generation. If 	 * omitted, default template is used. 	 */
annotation|@
name|Parameter
specifier|private
name|String
name|embeddableTemplate
decl_stmt|;
comment|/** 	 * If set to<code>true</code> (default), a directory tree will be generated 	 * in "destDir" corresponding to the class package structure, if set to 	 *<code>false</code>, classes will be generated in&quot;destDir&quot; 	 * ignoring their package. 	 */
annotation|@
name|Parameter
specifier|private
name|Boolean
name|usePkgPath
decl_stmt|;
comment|/**      * If set to<code>true</code>, will generate String Property names.      * Default is<code>false</code>.      */
annotation|@
name|Parameter
specifier|private
name|Boolean
name|createPropertyNames
decl_stmt|;
comment|/** 	 * If set to<code>true</code>, will skip file modification time validation and regenerate all. 	 * Default is<code>false</code>. 	 * 	 * @since 4.1 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|property
operator|=
literal|"force"
argument_list|)
specifier|private
name|boolean
name|force
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|String
name|queryTemplate
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|String
name|querySuperTemplate
decl_stmt|;
comment|/**      * If set to<code>true</code>, will generate PK attributes as Properties.      * Default is<code>false</code>.      * @since 4.1      */
annotation|@
name|Parameter
specifier|private
name|Boolean
name|createPKProperties
decl_stmt|;
specifier|private
specifier|transient
name|Injector
name|injector
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CayenneGeneratorMojo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|useConfigFromDataMap
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
comment|// Create the destination directory if necessary.
comment|// TODO: (KJM 11/2/06) The destDir really should be added as a
comment|// compilation resource for maven.
name|injector
operator|=
operator|new
name|ToolsInjectorBuilder
argument_list|()
operator|.
name|addModule
argument_list|(
operator|new
name|ToolsModule
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CayenneGeneratorMojo
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|Logger
name|logger
init|=
operator|new
name|MavenLogger
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|CayenneGeneratorMapLoaderAction
name|loaderAction
init|=
operator|new
name|CayenneGeneratorMapLoaderAction
argument_list|(
name|injector
argument_list|)
decl_stmt|;
name|loaderAction
operator|.
name|setMainDataMapFile
argument_list|(
name|map
argument_list|)
expr_stmt|;
try|try
block|{
name|loaderAction
operator|.
name|setAdditionalDataMapFiles
argument_list|(
name|convertAdditionalDataMaps
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|loaderAction
operator|.
name|getMainDataMap
argument_list|()
decl_stmt|;
name|ClassGenerationAction
name|generator
init|=
name|createGenerator
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|CayenneGeneratorEntityFilterAction
name|filterEntityAction
init|=
operator|new
name|CayenneGeneratorEntityFilterAction
argument_list|()
decl_stmt|;
name|filterEntityAction
operator|.
name|setNameFilter
argument_list|(
name|NamePatternMatcher
operator|.
name|build
argument_list|(
name|logger
argument_list|,
name|includeEntities
argument_list|,
name|excludeEntities
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneGeneratorEmbeddableFilterAction
name|filterEmbeddableAction
init|=
operator|new
name|CayenneGeneratorEmbeddableFilterAction
argument_list|()
decl_stmt|;
name|filterEmbeddableAction
operator|.
name|setNameFilter
argument_list|(
name|NamePatternMatcher
operator|.
name|build
argument_list|(
name|logger
argument_list|,
literal|null
argument_list|,
name|excludeEmbeddables
argument_list|)
argument_list|)
expr_stmt|;
name|filterEntityAction
operator|.
name|setClient
argument_list|(
name|generator
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|isClient
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
if|if
condition|(
name|force
condition|)
block|{
comment|// will (re-)generate all files
name|generator
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|generator
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|setTimestamp
argument_list|(
name|map
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasConfig
argument_list|()
operator|&&
name|useConfigFromDataMap
condition|)
block|{
name|generator
operator|.
name|prepareArtifacts
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|generator
operator|.
name|addEntities
argument_list|(
name|filterEntityAction
operator|.
name|getFilteredEntities
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|addEmbeddables
argument_list|(
name|filterEmbeddableAction
operator|.
name|getFilteredEmbeddables
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|addQueries
argument_list|(
name|dataMap
operator|.
name|getQueryDescriptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|generator
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
name|MojoExecutionException
argument_list|(
literal|"Error generating classes: "
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** 	 * Loads and returns DataMap based on<code>cgenConfiguration</code> attribute. 	 */
specifier|protected
name|File
index|[]
name|convertAdditionalDataMaps
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|additionalMaps
operator|==
literal|null
condition|)
block|{
return|return
name|NO_FILES
return|;
block|}
if|if
condition|(
operator|!
name|additionalMaps
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"'additionalMaps' must be a directory."
argument_list|)
throw|;
block|}
return|return
name|additionalMaps
operator|.
name|listFiles
argument_list|(
parameter_list|(
name|dir
parameter_list|,
name|name
parameter_list|)
lambda|->
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".map.xml"
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|hasConfig
parameter_list|()
block|{
return|return
name|destDir
operator|!=
literal|null
operator|||
name|encoding
operator|!=
literal|null
operator|||
name|client
operator|!=
literal|null
operator|||
name|excludeEntities
operator|!=
literal|null
operator|||
name|excludeEmbeddables
operator|!=
literal|null
operator|||
name|includeEntities
operator|!=
literal|null
operator|||
name|makePairs
operator|!=
literal|null
operator|||
name|mode
operator|!=
literal|null
operator|||
name|outputPattern
operator|!=
literal|null
operator|||
name|overwrite
operator|!=
literal|null
operator|||
name|superPkg
operator|!=
literal|null
operator|||
name|superTemplate
operator|!=
literal|null
operator|||
name|template
operator|!=
literal|null
operator|||
name|embeddableTemplate
operator|!=
literal|null
operator|||
name|embeddableSuperTemplate
operator|!=
literal|null
operator|||
name|usePkgPath
operator|!=
literal|null
operator|||
name|createPropertyNames
operator|!=
literal|null
operator|||
name|force
operator|||
name|queryTemplate
operator|!=
literal|null
operator|||
name|querySuperTemplate
operator|!=
literal|null
operator|||
name|createPKProperties
operator|!=
literal|null
return|;
block|}
comment|/** 	 * Factory method to create internal class generator. Called from 	 * constructor. 	 */
specifier|private
name|ClassGenerationAction
name|createGenerator
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|buildConfiguration
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|ClassGenerationActionFactory
operator|.
name|class
argument_list|)
operator|.
name|createAction
argument_list|(
name|cgenConfiguration
argument_list|)
return|;
block|}
specifier|private
name|CgenConfiguration
name|buildConfiguration
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataChannelMetaData
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasConfig
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using cgen config from pom.xml"
argument_list|)
expr_stmt|;
return|return
name|cgenConfigFromPom
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
if|else if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using cgen config from "
operator|+
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|useConfigFromDataMap
operator|=
literal|true
expr_stmt|;
return|return
name|cgenConfiguration
return|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using default cgen config."
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|=
operator|new
name|CgenConfiguration
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setRelPath
argument_list|(
name|defaultDir
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cgenConfiguration
return|;
block|}
block|}
specifier|private
name|CgenConfiguration
name|cgenConfigFromPom
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
operator|new
name|CgenConfiguration
argument_list|(
name|client
operator|!=
literal|null
condition|?
name|client
else|:
literal|false
argument_list|)
decl_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setRelPath
argument_list|(
name|destDir
operator|!=
literal|null
condition|?
name|destDir
operator|.
name|getPath
argument_list|()
else|:
name|defaultDir
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEncoding
argument_list|(
name|encoding
operator|!=
literal|null
condition|?
name|encoding
else|:
name|cgenConfiguration
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setMakePairs
argument_list|(
name|makePairs
operator|!=
literal|null
condition|?
name|makePairs
else|:
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
operator|&&
name|mode
operator|.
name|equals
argument_list|(
literal|"datamap"
argument_list|)
condition|)
block|{
name|replaceDatamapGenerationMode
argument_list|()
expr_stmt|;
block|}
name|cgenConfiguration
operator|.
name|setArtifactsGenerationMode
argument_list|(
name|mode
operator|!=
literal|null
condition|?
name|mode
else|:
name|cgenConfiguration
operator|.
name|getArtifactsGenerationMode
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setOutputPattern
argument_list|(
name|outputPattern
operator|!=
literal|null
condition|?
name|outputPattern
else|:
name|cgenConfiguration
operator|.
name|getOutputPattern
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setOverwrite
argument_list|(
name|overwrite
operator|!=
literal|null
condition|?
name|overwrite
else|:
name|cgenConfiguration
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setSuperPkg
argument_list|(
name|superPkg
operator|!=
literal|null
condition|?
name|superPkg
else|:
name|cgenConfiguration
operator|.
name|getSuperPkg
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setSuperTemplate
argument_list|(
name|superTemplate
operator|!=
literal|null
condition|?
name|superTemplate
else|:
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|template
operator|!=
literal|null
condition|?
name|template
else|:
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
name|embeddableSuperTemplate
operator|!=
literal|null
condition|?
name|embeddableSuperTemplate
else|:
name|cgenConfiguration
operator|.
name|getEmbeddableSuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|embeddableTemplate
operator|!=
literal|null
condition|?
name|embeddableTemplate
else|:
name|cgenConfiguration
operator|.
name|getEmbeddableTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setUsePkgPath
argument_list|(
name|usePkgPath
operator|!=
literal|null
condition|?
name|usePkgPath
else|:
name|cgenConfiguration
operator|.
name|isUsePkgPath
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setCreatePropertyNames
argument_list|(
name|createPropertyNames
operator|!=
literal|null
condition|?
name|createPropertyNames
else|:
name|cgenConfiguration
operator|.
name|isCreatePropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|queryTemplate
operator|!=
literal|null
condition|?
name|queryTemplate
else|:
name|cgenConfiguration
operator|.
name|getQueryTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQuerySuperTemplate
argument_list|(
name|querySuperTemplate
operator|!=
literal|null
condition|?
name|querySuperTemplate
else|:
name|cgenConfiguration
operator|.
name|getQuerySuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setCreatePKProperties
argument_list|(
name|createPKProperties
operator|!=
literal|null
condition|?
name|createPKProperties
else|:
name|cgenConfiguration
operator|.
name|isCreatePKProperties
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
condition|)
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
condition|?
name|ClientClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
else|:
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddableTemplate
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|queryTemplate
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
condition|?
name|ClientClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
else|:
name|ClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cgenConfiguration
return|;
block|}
specifier|private
name|void
name|replaceDatamapGenerationMode
parameter_list|()
block|{
name|this
operator|.
name|mode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ALL
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|excludeEntities
operator|=
literal|"*"
expr_stmt|;
name|this
operator|.
name|excludeEmbeddables
operator|=
literal|"*"
expr_stmt|;
name|this
operator|.
name|includeEntities
operator|=
literal|""
expr_stmt|;
block|}
block|}
end_class

end_unit

