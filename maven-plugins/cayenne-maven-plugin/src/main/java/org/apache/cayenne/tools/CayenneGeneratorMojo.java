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
name|DIBootstrap
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
name|slf4j
operator|.
name|Logger
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
name|LoggerFactory
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

begin_comment
comment|/**  * Maven mojo to perform class generation from data map. This class is an Maven  * adapter to DefaultClassGenerator class.  *   * @since 3.0  */
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
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
name|client
decl_stmt|;
comment|/** 	 * Destination directory for Java classes (ignoring their package names). 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.sourceDirectory}"
argument_list|)
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
comment|/** 	 * If set to<code>true</code>, will generate subclass/superclass pairs, 	 * with all generated code included in superclass (default is 	 *<code>true</code>). 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
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
argument_list|(
name|defaultValue
operator|=
literal|"entity"
argument_list|)
specifier|private
name|String
name|mode
decl_stmt|;
comment|/** 	 * Name of file for generated output. (Default is&quot;*.java&quot;) 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"*.java"
argument_list|)
specifier|private
name|String
name|outputPattern
decl_stmt|;
comment|/** 	 * If set to<code>true</code>, will overwrite older versions of generated 	 * classes. Ignored unless makepairs is set to<code>false</code>. 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
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
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|usePkgPath
decl_stmt|;
comment|/**      * If set to<code>true</code>, will generate String Property names.      * Default is<code>false</code>.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
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
comment|/** 	 * If set to<code>true</code>, will generate PK attributes as Properties. 	 * Default is<code>false</code>. 	 * @since 4.1 	 */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
name|createPKProperties
decl_stmt|;
specifier|private
specifier|transient
name|Injector
name|injector
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
if|if
condition|(
operator|!
name|destDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|destDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
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
argument_list|()
decl_stmt|;
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
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|generator
operator|.
name|setTimestamp
argument_list|(
name|map
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|generator
operator|.
name|addEntities
argument_list|(
name|filterAction
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
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
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
comment|/** 	 * Loads and returns DataMap based on<code>map</code> attribute. 	 */
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
comment|/** 	 * Factory method to create internal class generator. Called from 	 * constructor. 	 */
specifier|protected
name|ClassGenerationAction
name|createGenerator
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
block|}
else|else
block|{
name|action
operator|=
operator|new
name|ClassGenerationAction
argument_list|()
expr_stmt|;
block|}
name|injector
operator|.
name|injectMembers
argument_list|(
name|action
argument_list|)
expr_stmt|;
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
name|makePairs
argument_list|)
expr_stmt|;
name|action
operator|.
name|setArtifactsGenerationMode
argument_list|(
name|mode
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
name|superPkg
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSuperTemplate
argument_list|(
name|superTemplate
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
name|setEmbeddableSuperTemplate
argument_list|(
name|embeddableSuperTemplate
argument_list|)
expr_stmt|;
name|action
operator|.
name|setEmbeddableTemplate
argument_list|(
name|embeddableTemplate
argument_list|)
expr_stmt|;
name|action
operator|.
name|setUsePkgPath
argument_list|(
name|usePkgPath
argument_list|)
expr_stmt|;
name|action
operator|.
name|setCreatePropertyNames
argument_list|(
name|createPropertyNames
argument_list|)
expr_stmt|;
name|action
operator|.
name|setCreatePKProperties
argument_list|(
name|createPKProperties
argument_list|)
expr_stmt|;
return|return
name|action
return|;
block|}
block|}
end_class

end_unit

