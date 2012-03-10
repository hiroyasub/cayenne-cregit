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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
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

begin_comment
comment|/**  * Maven mojo to perform class generation from data map. This class is an Maven  * adapter to DefaultClassGenerator class.  *   * @since 3.0  *   * @phase generate-sources  * @goal cgen  */
end_comment

begin_class
specifier|public
class|class
name|CayenneGeneratorMojo
extends|extends
name|AbstractMojo
block|{
comment|/** 	 * Path to additional DataMap XML files to use for class generation. 	 *  	 * @parameter expression="${cgen.additionalMaps}" 	 */
specifier|private
name|File
name|additionalMaps
decl_stmt|;
comment|/** 	 * Whether we are generating classes for the client tier in a Remote Object 	 * Persistence application. Default is<code>false</code>. 	 *  	 * @parameter expression="${cgen.client}" default-value="false" 	 */
specifier|private
name|boolean
name|client
decl_stmt|;
comment|/** 	 * Destination directory for Java classes (ignoring their package names). 	 *  	 * @parameter expression="${cgen.destDir}" default-value= 	 *            "${project.build.sourceDirectory}/java/generated-sources/cayenne" 	 */
specifier|private
name|File
name|destDir
decl_stmt|;
comment|/** 	 * Specify generated file encoding if different from the default on current 	 * platform. Target encoding must be supported by the JVM running Maven 	 * build. Standard encodings supported by Java on all platforms are 	 * US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. See Sun Java 	 * Docs for java.nio.charset.Charset for more information. 	 *  	 * @parameter expression="${cgen.encoding}" 	 */
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/** 	 * Entities (expressed as a perl5 regex) to exclude from template 	 * generation. (Default is to include all entities in the DataMap). 	 *  	 * @parameter expression="${cgen.excludeEntities}" 	 */
specifier|private
name|String
name|excludeEntities
decl_stmt|;
comment|/** 	 * Entities (expressed as a perl5 regex) to include in template generation. 	 * (Default is to include all entities in the DataMap). 	 *  	 * @parameter expression="${cgen.includeEntities}" 	 */
specifier|private
name|String
name|includeEntities
decl_stmt|;
comment|/** 	 * If set to<code>true</code>, will generate subclass/superclass pairs, 	 * with all generated code included in superclass (default is 	 *<code>true</code>). 	 *  	 * @parameter expression="${cgen.makePairs}" default-value="true" 	 */
specifier|private
name|boolean
name|makePairs
decl_stmt|;
comment|/** 	 * DataMap XML file to use as a base for class generation. 	 *  	 * @parameter expression="${cgen.map}" 	 * @required 	 */
specifier|private
name|File
name|map
decl_stmt|;
comment|/** 	 * Specifies generator iteration target.&quot;entity&quot; performs one 	 * iteration for each selected entity.&quot;datamap&quot; performs one 	 * iteration per datamap (This is always one iteration since cgen currently 	 * supports specifying one-and-only-one datamap). (Default is 	 *&quot;entity&quot;) 	 *  	 * @parameter expression="${cgen.mode}" default-value="entity" 	 */
specifier|private
name|String
name|mode
decl_stmt|;
comment|/** 	 * Name of file for generated output. (Default is&quot;*.java&quot;) 	 *  	 * @parameter expression="${cgen.outputPattern}" default-value="*.java" 	 */
specifier|private
name|String
name|outputPattern
decl_stmt|;
comment|/** 	 * If set to<code>true</code>, will overwrite older versions of generated 	 * classes. Ignored unless makepairs is set to<code>false</code>. 	 *  	 * @parameter expression="${cgen.overwrite}" default-value="false" 	 */
specifier|private
name|boolean
name|overwrite
decl_stmt|;
comment|/** 	 * Java package name of generated superclasses. Ignored unless 	 *<code>makepairs</code> set to<code>true</code>. If omitted, each 	 * superclass will be assigned the same package as subclass. Note that 	 * having superclass in a different package would only make sense when 	 *<code>usepkgpath</code> is set to<code>true</code>. Otherwise classes 	 * from different packages will end up in the same directory. 	 *  	 * @parameter expression="${cgen.superPkg}" 	 */
specifier|private
name|String
name|superPkg
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Entity superclass generation. 	 * Ignored unless<code>makepairs</code> set to<code>true</code>. If 	 * omitted, default template is used. 	 *  	 * @parameter expression="${cgen.superTemplate}" 	 */
specifier|private
name|String
name|superTemplate
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Entity class generation. If 	 * omitted, default template is used. 	 *  	 * @parameter expression="${cgen.template}" 	 */
specifier|private
name|String
name|template
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Embeddable superclass generation. 	 * Ignored unless<code>makepairs</code> set to<code>true</code>. If 	 * omitted, default template is used. 	 *  	 * @parameter expression="${cgen.embeddableSuperTemplate}" 	 */
specifier|private
name|String
name|embeddableSuperTemplate
decl_stmt|;
comment|/** 	 * Location of Velocity template file for Embeddable class generation. If 	 * omitted, default template is used. 	 *  	 * @parameter expression="${cgen.embeddableTemplate}" 	 */
specifier|private
name|String
name|embeddableTemplate
decl_stmt|;
comment|/** 	 * If set to<code>true</code> (default), a directory tree will be generated 	 * in "destDir" corresponding to the class package structure, if set to 	 *<code>false</code>, classes will be generated in&quot;destDir&quot; 	 * ignoring their package. 	 *  	 * @parameter expression="${cgen.usePkgPath}" default-value="true" 	 */
specifier|private
name|boolean
name|usePkgPath
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
name|Log
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
argument_list|()
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
operator|new
name|NamePatternMatcher
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
comment|// ksenia khailenko 15.10.2010
comment|// TODO add the "includeEmbeddables" and "excludeEmbeddables"
comment|// attributes
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
comment|// TODO add the "includeQueries" and "excludeQueries" attributes
name|generator
operator|.
name|addQueries
argument_list|(
name|dataMap
operator|.
name|getQueries
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
literal|null
operator|==
name|additionalMaps
condition|)
block|{
return|return
operator|new
name|File
index|[
literal|0
index|]
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
literal|"'additionalMaps' must be a directory containing only datamap files."
argument_list|)
throw|;
block|}
name|String
index|[]
name|maps
init|=
name|additionalMaps
operator|.
name|list
argument_list|()
decl_stmt|;
name|File
index|[]
name|dataMaps
init|=
operator|new
name|File
index|[
name|maps
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|maps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|dataMaps
index|[
name|i
index|]
operator|=
operator|new
name|File
argument_list|(
name|maps
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|dataMaps
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
return|return
name|action
return|;
block|}
block|}
end_class

end_unit

