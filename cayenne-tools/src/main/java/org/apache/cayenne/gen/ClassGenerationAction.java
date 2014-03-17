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
name|gen
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
name|FileOutputStream
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
name|Writer
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|Embeddable
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
name|cayenne
operator|.
name|query
operator|.
name|Query
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
name|tools
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
name|velocity
operator|.
name|Template
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
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|app
operator|.
name|VelocityEngine
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
name|runtime
operator|.
name|RuntimeConstants
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
name|runtime
operator|.
name|log
operator|.
name|NullLogSystem
import|;
end_import

begin_class
specifier|public
class|class
name|ClassGenerationAction
block|{
specifier|static
specifier|final
name|String
name|TEMPLATES_DIR_NAME
init|=
literal|"templates/v1_2/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SINGLE_CLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"superclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"embeddable-singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDABLE_SUBCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"embeddable-subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDABLE_SUPERCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"embeddable-superclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATAMAP_SINGLE_CLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"datamap-singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATAMAP_SUBCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"datamap-subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATAMAP_SUPERCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"datamap-superclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_PREFIX
init|=
literal|"_"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WILDCARD
init|=
literal|"*"
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|artifacts
decl_stmt|;
specifier|protected
name|String
name|superPkg
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|ArtifactsGenerationMode
name|artifactsGenerationMode
decl_stmt|;
specifier|protected
name|boolean
name|makePairs
decl_stmt|;
specifier|protected
name|Log
name|logger
decl_stmt|;
specifier|protected
name|File
name|destDir
decl_stmt|;
specifier|protected
name|boolean
name|overwrite
decl_stmt|;
specifier|protected
name|boolean
name|usePkgPath
decl_stmt|;
specifier|protected
name|String
name|template
decl_stmt|;
specifier|protected
name|String
name|superTemplate
decl_stmt|;
specifier|protected
name|String
name|embeddableTemplate
decl_stmt|;
specifier|protected
name|String
name|embeddableSuperTemplate
decl_stmt|;
specifier|protected
name|String
name|queryTemplate
decl_stmt|;
specifier|protected
name|String
name|querySuperTemplate
decl_stmt|;
specifier|protected
name|long
name|timestamp
decl_stmt|;
specifier|protected
name|String
name|outputPattern
decl_stmt|;
specifier|protected
name|String
name|encoding
decl_stmt|;
comment|// runtime ivars
specifier|protected
name|VelocityContext
name|context
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Template
argument_list|>
name|templateCache
decl_stmt|;
specifier|public
name|ClassGenerationAction
parameter_list|()
block|{
name|this
operator|.
name|outputPattern
operator|=
literal|"*.java"
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|this
operator|.
name|usePkgPath
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|makePairs
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|context
operator|=
operator|new
name|VelocityContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|templateCache
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Template
argument_list|>
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|this
operator|.
name|artifacts
operator|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|String
name|defaultTemplateName
parameter_list|(
name|TemplateType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|ENTITY_SINGLE_CLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
return|;
case|case
name|ENTITY_SUBCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
return|;
case|case
name|ENTITY_SUPERCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
return|;
case|case
name|EMBEDDABLE_SUBCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUBCLASS_TEMPLATE
return|;
case|case
name|EMBEDDABLE_SUPERCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUPERCLASS_TEMPLATE
return|;
case|case
name|EMBEDDABLE_SINGLE_CLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
return|;
case|case
name|DATAMAP_SINGLE_CLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
return|;
case|case
name|DATAMAP_SUPERCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|DATAMAP_SUPERCLASS_TEMPLATE
return|;
case|case
name|DATAMAP_SUBCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|DATAMAP_SUBCLASS_TEMPLATE
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid template type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|customTemplateName
parameter_list|(
name|TemplateType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|ENTITY_SINGLE_CLASS
case|:
return|return
name|template
return|;
case|case
name|ENTITY_SUBCLASS
case|:
return|return
name|template
return|;
case|case
name|ENTITY_SUPERCLASS
case|:
return|return
name|superTemplate
return|;
case|case
name|EMBEDDABLE_SUBCLASS
case|:
return|return
name|embeddableTemplate
return|;
case|case
name|EMBEDDABLE_SUPERCLASS
case|:
return|return
name|embeddableSuperTemplate
return|;
case|case
name|DATAMAP_SINGLE_CLASS
case|:
return|return
name|queryTemplate
return|;
case|case
name|DATAMAP_SUPERCLASS
case|:
return|return
name|querySuperTemplate
return|;
case|case
name|DATAMAP_SUBCLASS
case|:
return|return
name|queryTemplate
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid template type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a String used to prefix class name to create a generated superclass.      * Default value is "_".      */
specifier|protected
name|String
name|getSuperclassPrefix
parameter_list|()
block|{
return|return
name|ClassGenerationAction
operator|.
name|SUPERCLASS_PREFIX
return|;
block|}
comment|/**      * VelocityContext initialization method called once per artifact.      */
specifier|protected
name|void
name|resetContextForArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|qualifiedClassName
init|=
name|artifact
operator|.
name|getQualifiedClassName
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|qualifiedClassName
argument_list|)
decl_stmt|;
name|String
name|className
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|qualifiedClassName
argument_list|)
decl_stmt|;
name|String
name|qualifiedBaseClassName
init|=
name|artifact
operator|.
name|getQualifiedBaseClassName
argument_list|()
decl_stmt|;
name|String
name|basePackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|qualifiedBaseClassName
argument_list|)
decl_stmt|;
name|String
name|baseClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|qualifiedBaseClassName
argument_list|)
decl_stmt|;
name|String
name|superClassName
init|=
name|getSuperclassPrefix
argument_list|()
operator|+
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|qualifiedClassName
argument_list|)
decl_stmt|;
name|String
name|superPackageName
init|=
name|this
operator|.
name|superPkg
decl_stmt|;
if|if
condition|(
name|superPackageName
operator|==
literal|null
condition|)
block|{
name|superPackageName
operator|=
name|packageName
operator|+
literal|".auto"
expr_stmt|;
block|}
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|BASE_CLASS_KEY
argument_list|,
name|baseClassName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|BASE_PACKAGE_KEY
argument_list|,
name|basePackageName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUB_CLASS_KEY
argument_list|,
name|className
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUB_PACKAGE_KEY
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUPER_CLASS_KEY
argument_list|,
name|superClassName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUPER_PACKAGE_KEY
argument_list|,
name|superPackageName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|OBJECT_KEY
argument_list|,
name|artifact
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|STRING_UTILS_KEY
argument_list|,
name|stringUtils
argument_list|)
expr_stmt|;
block|}
comment|/**      * VelocityContext initialization method called once per each artifact and template      * type combination.      */
specifier|protected
name|void
name|resetContextForArtifactTemplate
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|TemplateType
name|templateType
parameter_list|)
block|{
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|IMPORT_UTILS_KEY
argument_list|,
operator|new
name|ImportUtils
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|postInitContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Executes class generation once per each artifact.      */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|validateAttributes
argument_list|()
expr_stmt|;
try|try
block|{
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|execute
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// must reset engine at the end of class generator run to avoid memory
comment|// leaks and stale templates
name|this
operator|.
name|templateCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Executes class generation for a single artifact.      */
specifier|protected
name|void
name|execute
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|resetContextForArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|ArtifactGenerationMode
name|artifactMode
init|=
name|makePairs
condition|?
name|ArtifactGenerationMode
operator|.
name|GENERATION_GAP
else|:
name|ArtifactGenerationMode
operator|.
name|SINGLE_CLASS
decl_stmt|;
name|TemplateType
index|[]
name|templateTypes
init|=
name|artifact
operator|.
name|getTemplateTypes
argument_list|(
name|artifactMode
argument_list|)
decl_stmt|;
for|for
control|(
name|TemplateType
name|type
range|:
name|templateTypes
control|)
block|{
name|Writer
name|out
init|=
name|openWriter
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|resetContextForArtifactTemplate
argument_list|(
name|artifact
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|getTemplate
argument_list|(
name|type
argument_list|)
operator|.
name|merge
argument_list|(
name|context
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Template
name|getTemplate
parameter_list|(
name|TemplateType
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|templateName
init|=
name|customTemplateName
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|templateName
operator|==
literal|null
condition|)
block|{
name|templateName
operator|=
name|defaultTemplateName
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|// Velocity< 1.5 has some memory problems, so we will create a VelocityEngine
comment|// every time, and store templates in an internal cache, to avoid uncontrolled
comment|// memory leaks... Presumably 1.5 fixes it.
name|Template
name|template
init|=
name|templateCache
operator|.
name|get
argument_list|(
name|templateName
argument_list|)
decl_stmt|;
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// null logger that will prevent velocity.log from being generated
name|props
operator|.
name|put
argument_list|(
name|RuntimeConstants
operator|.
name|RUNTIME_LOG_LOGSYSTEM_CLASS
argument_list|,
name|NullLogSystem
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"resource.loader"
argument_list|,
literal|"cayenne"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.resource.loader.class"
argument_list|,
name|ClassGeneratorResourceLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.resource.loader.cache"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|VelocityEngine
name|velocityEngine
init|=
operator|new
name|VelocityEngine
argument_list|()
decl_stmt|;
name|velocityEngine
operator|.
name|init
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|template
operator|=
name|velocityEngine
operator|.
name|getTemplate
argument_list|(
name|templateName
argument_list|)
expr_stmt|;
name|templateCache
operator|.
name|put
argument_list|(
name|templateName
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
return|return
name|template
return|;
block|}
comment|/**      * Validates the state of this class generator. Throws CayenneRuntimeException if it      * is in an inconsistent state. Called internally from "execute".      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
block|{
if|if
condition|(
name|destDir
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'destDir' attribute is missing."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|destDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'destDir' is not a directory."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|destDir
operator|.
name|canWrite
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Do not have write permissions for "
operator|+
name|destDir
argument_list|)
throw|;
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
name|setMakePairs
parameter_list|(
name|boolean
name|makePairs
parameter_list|)
block|{
name|this
operator|.
name|makePairs
operator|=
name|makePairs
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
comment|/**      * Sets<code>superTemplate</code> property.      */
specifier|public
name|void
name|setSuperTemplate
parameter_list|(
name|String
name|superTemplate
parameter_list|)
block|{
name|this
operator|.
name|superTemplate
operator|=
name|superTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|setQueryTemplate
parameter_list|(
name|String
name|queryTemplate
parameter_list|)
block|{
name|this
operator|.
name|queryTemplate
operator|=
name|queryTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|setQuerySuperTemplate
parameter_list|(
name|String
name|querySuperTemplate
parameter_list|)
block|{
name|this
operator|.
name|querySuperTemplate
operator|=
name|querySuperTemplate
expr_stmt|;
block|}
comment|/**      * Sets<code>usepkgpath</code> property.      */
specifier|public
name|void
name|setUsePkgPath
parameter_list|(
name|boolean
name|usePkgPath
parameter_list|)
block|{
name|this
operator|.
name|usePkgPath
operator|=
name|usePkgPath
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
comment|/**      * Opens a Writer to write generated output. Returned Writer is mapped to a filesystem      * file (although subclasses may override that). File location is determined from the      * current state of VelocityContext and the TemplateType passed as a parameter. Writer      * encoding is determined from the value of the "encoding" property.      */
specifier|protected
name|Writer
name|openWriter
parameter_list|(
name|TemplateType
name|templateType
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|outFile
init|=
operator|(
name|templateType
operator|.
name|isSuperclass
argument_list|()
operator|)
condition|?
name|fileForSuperclass
argument_list|()
else|:
name|fileForClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|outFile
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|String
name|label
init|=
name|templateType
operator|.
name|isSuperclass
argument_list|()
condition|?
literal|"superclass"
else|:
literal|"class"
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Generating "
operator|+
name|label
operator|+
literal|" file: "
operator|+
name|outFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// return writer with specified encoding
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
return|return
operator|(
name|encoding
operator|!=
literal|null
operator|)
condition|?
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|encoding
argument_list|)
else|:
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|/**      * Returns a target file where a generated superclass must be saved. If null is      * returned, class shouldn't be generated.      */
specifier|protected
name|File
name|fileForSuperclass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUPER_PACKAGE_KEY
argument_list|)
decl_stmt|;
name|String
name|className
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUPER_CLASS_KEY
argument_list|)
decl_stmt|;
name|String
name|filename
init|=
name|NamePatternMatcher
operator|.
name|replaceWildcardInStringWithString
argument_list|(
name|WILDCARD
argument_list|,
name|outputPattern
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
name|mkpath
argument_list|(
name|destDir
argument_list|,
name|packageName
argument_list|)
argument_list|,
name|filename
argument_list|)
decl_stmt|;
comment|// Ignore if the destination is newer than the map
comment|// (internal timestamp), i.e. has been generated after the map was
comment|// last saved AND the template is older than the destination file
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|isOld
argument_list|(
name|dest
argument_list|)
condition|)
block|{
if|if
condition|(
name|superTemplate
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|superTemplateFile
init|=
operator|new
name|File
argument_list|(
name|superTemplate
argument_list|)
decl_stmt|;
if|if
condition|(
name|superTemplateFile
operator|.
name|lastModified
argument_list|()
operator|<
name|dest
operator|.
name|lastModified
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|dest
return|;
block|}
comment|/**      * Returns a target file where a generated class must be saved. If null is returned,      * class shouldn't be generated.      */
specifier|protected
name|File
name|fileForClass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUB_PACKAGE_KEY
argument_list|)
decl_stmt|;
name|String
name|className
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUB_CLASS_KEY
argument_list|)
decl_stmt|;
name|String
name|filename
init|=
name|NamePatternMatcher
operator|.
name|replaceWildcardInStringWithString
argument_list|(
name|WILDCARD
argument_list|,
name|outputPattern
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
name|mkpath
argument_list|(
name|destDir
argument_list|,
name|packageName
argument_list|)
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// no overwrite of subclasses
if|if
condition|(
name|makePairs
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// skip if said so
if|if
condition|(
operator|!
name|overwrite
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Ignore if the destination is newer than the map
comment|// (internal timestamp), i.e. has been generated after the map was
comment|// last saved AND the template is older than the destination file
if|if
condition|(
operator|!
name|isOld
argument_list|(
name|dest
argument_list|)
condition|)
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|templateFile
init|=
operator|new
name|File
argument_list|(
name|template
argument_list|)
decl_stmt|;
if|if
condition|(
name|templateFile
operator|.
name|lastModified
argument_list|()
operator|<
name|dest
operator|.
name|lastModified
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
return|return
name|dest
return|;
block|}
comment|/**      * Returns true if<code>file</code> parameter is older than internal timestamp of      * this class generator.      */
specifier|protected
name|boolean
name|isOld
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|file
operator|.
name|lastModified
argument_list|()
operator|<=
name|timestamp
return|;
block|}
comment|/**      * Returns a File object corresponding to a directory where files that belong to      *<code>pkgName</code> package should reside. Creates any missing diectories below      *<code>dest</code>.      */
specifier|protected
name|File
name|mkpath
parameter_list|(
name|File
name|dest
parameter_list|,
name|String
name|pkgName
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|usePkgPath
operator|||
name|pkgName
operator|==
literal|null
condition|)
block|{
return|return
name|dest
return|;
block|}
name|String
name|path
init|=
name|pkgName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
decl_stmt|;
name|File
name|fullPath
init|=
operator|new
name|File
argument_list|(
name|dest
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fullPath
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|fullPath
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error making path: "
operator|+
name|fullPath
argument_list|)
throw|;
block|}
return|return
name|fullPath
return|;
block|}
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
comment|/**      * Sets file encoding. If set to null, default system encoding will be used.      */
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
comment|/**      * Sets "superPkg" property value.      */
specifier|public
name|void
name|setSuperPkg
parameter_list|(
name|String
name|superPkg
parameter_list|)
block|{
name|this
operator|.
name|superPkg
operator|=
name|superPkg
expr_stmt|;
block|}
comment|/**      * @param dataMap The dataMap to set.      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/**      * Adds entities to the internal entity list.      */
specifier|public
name|void
name|addEntities
parameter_list|(
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
parameter_list|)
block|{
if|if
condition|(
name|artifactsGenerationMode
operator|==
name|ArtifactsGenerationMode
operator|.
name|ENTITY
operator|||
name|artifactsGenerationMode
operator|==
name|ArtifactsGenerationMode
operator|.
name|ALL
condition|)
block|{
if|if
condition|(
name|entities
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entities
control|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|EntityArtifact
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|addEmbeddables
parameter_list|(
name|Collection
argument_list|<
name|Embeddable
argument_list|>
name|embeddables
parameter_list|)
block|{
if|if
condition|(
name|artifactsGenerationMode
operator|==
name|ArtifactsGenerationMode
operator|.
name|ENTITY
operator|||
name|artifactsGenerationMode
operator|==
name|ArtifactsGenerationMode
operator|.
name|ALL
condition|)
block|{
if|if
condition|(
name|embeddables
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Embeddable
name|embeddable
range|:
name|embeddables
control|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|EmbeddableArtifact
argument_list|(
name|embeddable
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|addQueries
parameter_list|(
name|Collection
argument_list|<
name|Query
argument_list|>
name|queries
parameter_list|)
block|{
if|if
condition|(
name|artifactsGenerationMode
operator|==
name|ArtifactsGenerationMode
operator|.
name|DATAMAP
operator|||
name|artifactsGenerationMode
operator|==
name|ArtifactsGenerationMode
operator|.
name|ALL
condition|)
block|{
comment|// TODO: andrus 10.12.2010 - why not also check for empty query list?? Or
comment|// create a better API for enabling DataMapArtifact
if|if
condition|(
name|queries
operator|!=
literal|null
condition|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|DataMapArtifact
argument_list|(
name|dataMap
argument_list|,
name|queries
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Sets an optional shared VelocityContext. Useful with tools like VPP that can set      * custom values in the context, not known to Cayenne.      */
specifier|public
name|void
name|setContext
parameter_list|(
name|VelocityContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Injects an optional logger that will be used to trace generated files at the info      * level.      */
specifier|public
name|void
name|setLogger
parameter_list|(
name|Log
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
specifier|public
name|void
name|setEmbeddableTemplate
parameter_list|(
name|String
name|embeddableTemplate
parameter_list|)
block|{
name|this
operator|.
name|embeddableTemplate
operator|=
name|embeddableTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|setEmbeddableSuperTemplate
parameter_list|(
name|String
name|embeddableSuperTemplate
parameter_list|)
block|{
name|this
operator|.
name|embeddableSuperTemplate
operator|=
name|embeddableSuperTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactsGenerationMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
if|if
condition|(
name|ArtifactsGenerationMode
operator|.
name|ENTITY
operator|.
name|getLabel
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|mode
argument_list|)
condition|)
block|{
name|this
operator|.
name|artifactsGenerationMode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ENTITY
expr_stmt|;
block|}
if|else if
condition|(
name|ArtifactsGenerationMode
operator|.
name|DATAMAP
operator|.
name|getLabel
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|mode
argument_list|)
condition|)
block|{
name|this
operator|.
name|artifactsGenerationMode
operator|=
name|ArtifactsGenerationMode
operator|.
name|DATAMAP
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|artifactsGenerationMode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ALL
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

