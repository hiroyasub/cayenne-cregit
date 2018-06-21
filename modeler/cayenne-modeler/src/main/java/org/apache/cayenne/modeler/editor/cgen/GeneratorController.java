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
name|modeler
operator|.
name|editor
operator|.
name|cgen
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
name|map
operator|.
name|*
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|dialog
operator|.
name|pref
operator|.
name|GeneralPreferences
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
name|modeler
operator|.
name|pref
operator|.
name|DataMapDefaults
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
name|modeler
operator|.
name|pref
operator|.
name|FSPath
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
name|modeler
operator|.
name|util
operator|.
name|CayenneController
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
name|modeler
operator|.
name|util
operator|.
name|CodeValidationUtil
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
name|modeler
operator|.
name|util
operator|.
name|TextAdapter
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
name|swing
operator|.
name|BindingBuilder
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
name|validation
operator|.
name|BeanValidationFailure
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
name|validation
operator|.
name|SimpleValidationFailure
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
name|validation
operator|.
name|ValidationFailure
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
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
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_comment
comment|/**  * A mode-specific part of the code generation dialog.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|GeneratorController
extends|extends
name|CayenneController
block|{
specifier|protected
name|String
name|mode
init|=
name|ArtifactsGenerationMode
operator|.
name|ENTITY
operator|.
name|getLabel
argument_list|()
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
name|mapPreferences
decl_stmt|;
specifier|private
name|String
name|outputPath
decl_stmt|;
specifier|public
name|GeneratorController
parameter_list|(
name|CodeGeneratorControllerBase
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutputPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|old
init|=
name|this
operator|.
name|outputPath
decl_stmt|;
name|this
operator|.
name|outputPath
operator|=
name|path
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|outputPath
operator|!=
literal|null
operator|&&
operator|!
name|this
operator|.
name|outputPath
operator|.
name|equals
argument_list|(
name|old
argument_list|)
condition|)
block|{
name|updatePreferences
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|updatePreferences
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|mapPreferences
operator|==
literal|null
condition|)
return|return;
name|Set
argument_list|<
name|DataMap
argument_list|>
name|keys
init|=
name|mapPreferences
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|DataMap
name|key
range|:
name|keys
control|)
block|{
name|mapPreferences
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|setOutputPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|initBindings
parameter_list|(
name|BindingBuilder
name|bindingBuilder
parameter_list|)
block|{
name|JButton
name|outputSelect
init|=
operator|(
operator|(
name|GeneratorControllerPanel
operator|)
name|getView
argument_list|()
operator|)
operator|.
name|getSelectOutputFolder
argument_list|()
decl_stmt|;
name|bindingBuilder
operator|.
name|bindToAction
argument_list|(
name|outputSelect
argument_list|,
literal|"selectOutputFolderAction()"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CodeGeneratorControllerBase
name|getParentController
parameter_list|()
block|{
return|return
operator|(
name|CodeGeneratorControllerBase
operator|)
name|getParent
argument_list|()
return|;
block|}
comment|/**      * Creates an appropriate subclass of {@link ClassGenerationAction},      * returning it in an unconfigured state. Configuration is performed by      * {@link #createGenerator()} method.      */
specifier|protected
specifier|abstract
name|ClassGenerationAction
name|newGenerator
parameter_list|()
function_decl|;
comment|/**      * Creates a class generator for provided selections.      */
specifier|public
name|ClassGenerationAction
name|createGenerator
parameter_list|()
block|{
name|DataMap
name|map
init|=
name|getParentController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|ClassGenerationAction
name|generator
init|=
name|getParentController
argument_list|()
operator|.
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|map
argument_list|,
name|ClassGenerationAction
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|generator
operator|!=
literal|null
condition|)
block|{
name|getParentController
argument_list|()
operator|.
name|addToSelectedEntities
argument_list|(
name|generator
operator|.
name|getEntities
argument_list|()
argument_list|)
expr_stmt|;
name|getParentController
argument_list|()
operator|.
name|addToSelectedEmbeddables
argument_list|(
name|generator
operator|.
name|getEmbeddables
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|generator
return|;
block|}
try|try
block|{
name|generator
operator|=
name|newGenerator
argument_list|()
expr_stmt|;
name|generator
operator|.
name|setDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|initOutputFolder
argument_list|()
expr_stmt|;
name|File
name|outputDir
init|=
operator|new
name|File
argument_list|(
name|outputPath
argument_list|)
decl_stmt|;
comment|// no destination folder
if|if
condition|(
name|outputDir
operator|==
literal|null
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Select directory for source files."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// no such folder
if|if
condition|(
operator|!
name|outputDir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|outputDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Can't create directory "
operator|+
name|outputDir
operator|+
literal|". Select a different one."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// not a directory
if|if
condition|(
operator|!
name|outputDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
name|outputDir
operator|+
literal|" is not a valid directory."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|generator
operator|.
name|setDestDir
argument_list|(
name|outputDir
argument_list|)
expr_stmt|;
name|Preferences
name|preferences
init|=
name|application
operator|.
name|getPreferencesNode
argument_list|(
name|GeneralPreferences
operator|.
name|class
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferences
operator|!=
literal|null
condition|)
block|{
name|generator
operator|.
name|setEncoding
argument_list|(
name|preferences
operator|.
name|get
argument_list|(
name|GeneralPreferences
operator|.
name|ENCODING_PREFERENCE
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getParentController
argument_list|()
operator|.
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|add
argument_list|(
name|map
argument_list|,
name|generator
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|exception
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
name|exception
operator|.
name|getUnlabeledMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|generator
return|;
block|}
specifier|public
name|void
name|validateEmbeddable
parameter_list|(
name|ValidationResult
name|validationBuffer
parameter_list|,
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|ValidationFailure
name|embeddableFailure
init|=
name|validateEmbeddable
argument_list|(
name|embeddable
argument_list|)
decl_stmt|;
if|if
condition|(
name|embeddableFailure
operator|!=
literal|null
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
name|embeddableFailure
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|EmbeddableAttribute
name|attribute
range|:
name|embeddable
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|ValidationFailure
name|failure
init|=
name|validateEmbeddableAttribute
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|failure
operator|!=
literal|null
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
name|failure
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
specifier|private
name|ValidationFailure
name|validateEmbeddableAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
name|String
name|name
init|=
name|attribute
operator|.
name|getEmbeddable
argument_list|()
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|ValidationFailure
name|emptyName
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyName
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyName
return|;
block|}
name|ValidationFailure
name|badName
init|=
name|CodeValidationUtil
operator|.
name|validateJavaIdentifier
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badName
operator|!=
literal|null
condition|)
block|{
return|return
name|badName
return|;
block|}
name|ValidationFailure
name|emptyType
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.type"
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyType
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyType
return|;
block|}
name|ValidationFailure
name|badType
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"attribute.type"
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badType
operator|!=
literal|null
condition|)
block|{
return|return
name|badType
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ValidationFailure
name|validateEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|String
name|name
init|=
name|embeddable
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|ValidationFailure
name|emptyClass
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"className"
argument_list|,
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyClass
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyClass
return|;
block|}
name|ValidationFailure
name|badClass
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"className"
argument_list|,
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badClass
operator|!=
literal|null
condition|)
block|{
return|return
name|badClass
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|validateEntity
parameter_list|(
name|ValidationResult
name|validationBuffer
parameter_list|,
name|ObjEntity
name|entity
parameter_list|,
name|boolean
name|clientValidation
parameter_list|)
block|{
name|ValidationFailure
name|entityFailure
init|=
name|validateEntity
argument_list|(
name|clientValidation
condition|?
name|entity
operator|.
name|getClientEntity
argument_list|()
else|:
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|entityFailure
operator|!=
literal|null
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
name|entityFailure
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|ObjAttribute
name|attribute
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attribute
operator|instanceof
name|EmbeddedAttribute
condition|)
block|{
name|EmbeddedAttribute
name|embeddedAttribute
init|=
operator|(
name|EmbeddedAttribute
operator|)
name|attribute
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|subAttribute
range|:
name|embeddedAttribute
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|ValidationFailure
name|failure
init|=
name|validateEmbeddedAttribute
argument_list|(
name|subAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|failure
operator|!=
literal|null
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
name|failure
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
else|else
block|{
name|ValidationFailure
name|failure
init|=
name|validateAttribute
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|failure
operator|!=
literal|null
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
name|failure
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
for|for
control|(
name|ObjRelationship
name|rel
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|ValidationFailure
name|failure
init|=
name|validateRelationship
argument_list|(
name|rel
argument_list|,
name|clientValidation
argument_list|)
decl_stmt|;
if|if
condition|(
name|failure
operator|!=
literal|null
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
name|failure
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
specifier|protected
name|ValidationFailure
name|validateEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|String
name|name
init|=
name|entity
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|isGeneric
argument_list|()
condition|)
block|{
return|return
operator|new
name|SimpleValidationFailure
argument_list|(
name|name
argument_list|,
literal|"Generic class"
argument_list|)
return|;
block|}
name|ValidationFailure
name|emptyClass
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"className"
argument_list|,
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyClass
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyClass
return|;
block|}
name|ValidationFailure
name|badClass
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"className"
argument_list|,
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badClass
operator|!=
literal|null
condition|)
block|{
return|return
name|badClass
return|;
block|}
if|if
condition|(
name|entity
operator|.
name|getSuperClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ValidationFailure
name|badSuperClass
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"superClassName"
argument_list|,
name|entity
operator|.
name|getSuperClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badSuperClass
operator|!=
literal|null
condition|)
block|{
return|return
name|badSuperClass
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ValidationFailure
name|validateAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|String
name|name
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ValidationFailure
name|emptyName
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyName
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyName
return|;
block|}
name|ValidationFailure
name|badName
init|=
name|CodeValidationUtil
operator|.
name|validateJavaIdentifier
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badName
operator|!=
literal|null
condition|)
block|{
return|return
name|badName
return|;
block|}
name|ValidationFailure
name|emptyType
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.type"
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyType
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyType
return|;
block|}
name|ValidationFailure
name|badType
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"attribute.type"
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badType
operator|!=
literal|null
condition|)
block|{
return|return
name|badType
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ValidationFailure
name|validateEmbeddedAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|String
name|name
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// validate embeddedAttribute and attribute names
comment|// embeddedAttribute returned attibute as
comment|// [name_embeddedAttribute].[name_attribute]
name|String
index|[]
name|attributes
init|=
name|attribute
operator|.
name|getName
argument_list|()
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
name|String
name|nameEmbeddedAttribute
init|=
name|attributes
index|[
literal|0
index|]
decl_stmt|;
name|int
name|beginIndex
init|=
name|attributes
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
decl_stmt|;
name|String
name|attr
init|=
name|attribute
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|beginIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
name|ValidationFailure
name|emptyEmbeddedName
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|nameEmbeddedAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyEmbeddedName
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyEmbeddedName
return|;
block|}
name|ValidationFailure
name|badEmbeddedName
init|=
name|CodeValidationUtil
operator|.
name|validateJavaIdentifier
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|nameEmbeddedAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|badEmbeddedName
operator|!=
literal|null
condition|)
block|{
return|return
name|badEmbeddedName
return|;
block|}
name|ValidationFailure
name|emptyName
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|attr
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyName
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyName
return|;
block|}
name|ValidationFailure
name|badName
init|=
name|CodeValidationUtil
operator|.
name|validateJavaIdentifier
argument_list|(
name|name
argument_list|,
literal|"attribute.name"
argument_list|,
name|attr
argument_list|)
decl_stmt|;
if|if
condition|(
name|badName
operator|!=
literal|null
condition|)
block|{
return|return
name|badName
return|;
block|}
name|ValidationFailure
name|emptyType
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"attribute.type"
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyType
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyType
return|;
block|}
name|ValidationFailure
name|badType
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"attribute.type"
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badType
operator|!=
literal|null
condition|)
block|{
return|return
name|badType
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ValidationFailure
name|validateRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|boolean
name|clientValidation
parameter_list|)
block|{
name|String
name|name
init|=
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ValidationFailure
name|emptyName
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"relationship.name"
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyName
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyName
return|;
block|}
name|ValidationFailure
name|badName
init|=
name|CodeValidationUtil
operator|.
name|validateJavaIdentifier
argument_list|(
name|name
argument_list|,
literal|"relationship.name"
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badName
operator|!=
literal|null
condition|)
block|{
return|return
name|badName
return|;
block|}
if|if
condition|(
operator|!
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|ObjEntity
name|targetEntity
init|=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientValidation
operator|&&
name|targetEntity
operator|!=
literal|null
condition|)
block|{
name|targetEntity
operator|=
name|targetEntity
operator|.
name|getClientEntity
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|targetEntity
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|BeanValidationFailure
argument_list|(
name|name
argument_list|,
literal|"relationship.targetEntity"
argument_list|,
literal|"No target entity"
argument_list|)
return|;
block|}
if|else if
condition|(
operator|!
name|targetEntity
operator|.
name|isGeneric
argument_list|()
condition|)
block|{
name|ValidationFailure
name|emptyClass
init|=
name|BeanValidationFailure
operator|.
name|validateNotEmpty
argument_list|(
name|name
argument_list|,
literal|"relationship.targetEntity.className"
argument_list|,
name|targetEntity
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|emptyClass
operator|!=
literal|null
condition|)
block|{
return|return
name|emptyClass
return|;
block|}
name|ValidationFailure
name|badClass
init|=
name|BeanValidationFailure
operator|.
name|validateJavaClassName
argument_list|(
name|name
argument_list|,
literal|"relationship.targetEntity.className"
argument_list|,
name|targetEntity
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|badClass
operator|!=
literal|null
condition|)
block|{
return|return
name|badClass
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns a predicate for default entity selection in a given mode.      */
specifier|public
name|Predicate
name|getDefaultClassFilter
parameter_list|()
block|{
specifier|final
name|ObjEntity
name|selectedEntity
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
specifier|final
name|Embeddable
name|selectedEmbeddable
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getCurrentEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedEntity
operator|!=
literal|null
condition|)
block|{
comment|// select a single entity
specifier|final
name|boolean
name|hasProblem
init|=
name|getParentController
argument_list|()
operator|.
name|getProblem
argument_list|(
name|selectedEntity
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
decl_stmt|;
return|return
name|object
lambda|->
operator|!
name|hasProblem
operator|&&
name|object
operator|==
name|selectedEntity
return|;
block|}
if|else if
condition|(
name|selectedEmbeddable
operator|!=
literal|null
condition|)
block|{
comment|// select a single embeddable
specifier|final
name|boolean
name|hasProblem
init|=
name|getParentController
argument_list|()
operator|.
name|getProblem
argument_list|(
name|selectedEmbeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
operator|!=
literal|null
decl_stmt|;
return|return
name|object
lambda|->
operator|!
name|hasProblem
operator|&&
name|object
operator|==
name|selectedEmbeddable
return|;
block|}
else|else
block|{
comment|// select all entities
return|return
name|object
lambda|->
block|{
if|if
condition|(
name|object
operator|instanceof
name|ObjEntity
condition|)
block|{
return|return
name|getParentController
argument_list|()
operator|.
name|getProblem
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Embeddable
condition|)
block|{
return|return
name|getParentController
argument_list|()
operator|.
name|getProblem
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|object
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
operator|==
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
return|;
block|}
block|}
comment|/**      * An action method that pops up a file chooser dialog to pick the      * generation directory.      */
specifier|public
name|void
name|selectOutputFolderAction
parameter_list|()
block|{
name|TextAdapter
name|outputFolder
init|=
operator|(
operator|(
name|GeneratorControllerPanel
operator|)
name|getView
argument_list|()
operator|)
operator|.
name|getOutputFolder
argument_list|()
decl_stmt|;
name|String
name|currentDir
init|=
name|outputFolder
operator|.
name|getComponent
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
name|JFileChooser
name|chooser
init|=
operator|new
name|JFileChooser
argument_list|()
decl_stmt|;
name|chooser
operator|.
name|setFileSelectionMode
argument_list|(
name|JFileChooser
operator|.
name|DIRECTORIES_ONLY
argument_list|)
expr_stmt|;
name|chooser
operator|.
name|setDialogType
argument_list|(
name|JFileChooser
operator|.
name|OPEN_DIALOG
argument_list|)
expr_stmt|;
comment|// guess start directory
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|currentDir
argument_list|)
condition|)
block|{
name|chooser
operator|.
name|setCurrentDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|currentDir
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|FSPath
name|lastDir
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getLastDirectory
argument_list|()
decl_stmt|;
name|lastDir
operator|.
name|updateChooser
argument_list|(
name|chooser
argument_list|)
expr_stmt|;
block|}
name|int
name|result
init|=
name|chooser
operator|.
name|showOpenDialog
argument_list|(
name|getView
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
name|File
name|selected
init|=
name|chooser
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
comment|// update model
name|String
name|path
init|=
name|selected
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
operator|(
operator|(
name|GeneratorControllerPanel
operator|)
name|getView
argument_list|()
operator|)
operator|.
name|getOutputFolder
argument_list|()
operator|.
name|setText
argument_list|(
name|path
argument_list|)
expr_stmt|;
operator|(
operator|(
name|GeneratorControllerPanel
operator|)
name|getView
argument_list|()
operator|)
operator|.
name|getOutputFolder
argument_list|()
operator|.
name|updateModel
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initOutputFolder
parameter_list|()
block|{
name|String
name|pathString
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
decl_stmt|;
name|setOutputPath
argument_list|(
name|pathString
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|checkDefaultMavenResourceDir
parameter_list|(
name|FSPath
name|lastPath
parameter_list|,
name|String
name|dirType
parameter_list|)
block|{
name|String
name|path
init|=
name|lastPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|pathElements
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|(
name|pathElements
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|pathElements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|path
operator|.
name|append
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|.
name|append
argument_list|(
name|pathElements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

