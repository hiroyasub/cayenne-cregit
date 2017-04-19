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
operator|.
name|validation
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionException
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
name|DbAttribute
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
name|EmbeddableAttribute
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
name|EmbeddedAttribute
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
name|ObjAttribute
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
name|ValidationResult
import|;
end_import

begin_class
class|class
name|ObjAttributeValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|validateName
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
comment|// all attributes must have type
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjAttribute '%s' has no Java type"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attribute
operator|instanceof
name|EmbeddedAttribute
condition|)
block|{
name|validateEmbeddable
argument_list|(
operator|(
name|EmbeddedAttribute
operator|)
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|validateDbAttribute
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
name|checkForDuplicates
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|checkSuperEntityAttributes
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateName
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"Unnamed ObjAttribute"
argument_list|)
expr_stmt|;
return|return;
block|}
name|NameValidationHelper
name|helper
init|=
name|NameValidationHelper
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|invalidChars
init|=
name|helper
operator|.
name|invalidCharsInObjPathComponent
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalidChars
operator|!=
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjAttribute name '%s' contains invalid characters: %s"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|invalidChars
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|helper
operator|.
name|invalidDataObjectProperty
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjAttribute name '%s' is invalid"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkSuperEntityAttributes
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
comment|// Check there is an attribute in entity and super entity at the same time
name|boolean
name|selfAttribute
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|selfAttribute
operator|=
literal|true
expr_stmt|;
block|}
name|ObjEntity
name|superEntity
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getSuperEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|selfAttribute
operator|&&
name|superEntity
operator|!=
literal|null
operator|&&
name|superEntity
operator|.
name|getAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"'%s' and super '%s' can't have attribute '%s' together "
argument_list|,
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|superEntity
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|validateDbAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
comment|// nothing to validate
comment|// abstract entity does not have to define a dbAttribute
return|return;
block|}
name|DbAttribute
name|dbAttribute
decl_stmt|;
try|try
block|{
name|dbAttribute
operator|=
name|attribute
operator|.
name|getDbAttribute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
comment|// see CAY-2153
comment|// getDbAttribute() can fail if db path for this attribute is invalid
comment|// so we catch it here and show nice validation failure instead of crash
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjAttribute '%s' has invalid DB path: %s"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getExpressionString
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|dbAttribute
operator|==
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjAttribute '%s' has no DbAttribute mapping"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|dbAttribute
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|dbAttribute
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
comment|// can't support generated meaningful attributes for now;
comment|// besides they don't make sense.
comment|// TODO: andrus 03/10/2010 - is that really so? I think those are supported...
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjAttribute '%s' is mapped to a generated PK: %s"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|validateEmbeddable
parameter_list|(
name|EmbeddedAttribute
name|attribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|Embeddable
name|embeddable
init|=
name|attribute
operator|.
name|getEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddable
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
name|attribute
operator|.
name|getType
argument_list|()
operator|==
literal|null
condition|?
literal|"EmbeddedAttribute '%s' has no Embeddable"
else|:
literal|"EmbeddedAttribute '%s' has incorrect Embeddable"
decl_stmt|;
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
name|msg
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attrOverrides
init|=
name|attribute
operator|.
name|getAttributeOverrides
argument_list|()
decl_stmt|;
for|for
control|(
name|EmbeddableAttribute
name|embeddableAttribute
range|:
name|embeddable
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|String
name|dbAttributeName
decl_stmt|;
if|if
condition|(
operator|!
name|attrOverrides
operator|.
name|isEmpty
argument_list|()
operator|&&
name|attrOverrides
operator|.
name|containsKey
argument_list|(
name|embeddableAttribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|dbAttributeName
operator|=
name|attrOverrides
operator|.
name|get
argument_list|(
name|embeddableAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dbAttributeName
operator|=
name|embeddableAttribute
operator|.
name|getDbAttributeName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|dbAttributeName
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"EmbeddedAttribute '%s' has no DbAttribute mapping"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|dbAttributeName
argument_list|)
operator|==
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"EmbeddedAttribute '%s' has incorrect DbAttribute mapping"
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Per CAY-1813, make sure two (or more) ObjAttributes do not map to the      * same database path.      */
specifier|private
name|void
name|checkForDuplicates
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|!=
literal|null
operator|&&
name|attribute
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|attribute
operator|.
name|isInherited
argument_list|()
condition|)
block|{
name|ObjEntity
name|entity
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|comparisonAttribute
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
operator|!=
name|comparisonAttribute
condition|)
block|{
name|String
name|dbAttributePath
init|=
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttributePath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dbAttributePath
operator|.
name|equals
argument_list|(
name|comparisonAttribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|attribute
argument_list|,
literal|"ObjEntity '%s' contains a duplicate DbAttribute mapping ('%s' -> '%s')"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|dbAttributePath
argument_list|)
expr_stmt|;
return|return;
comment|// Duplicate found, stop.
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

