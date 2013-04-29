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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|DataChannelDescriptor
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
name|ObjEntityValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|validateName
argument_list|(
name|entity
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|validateClassName
argument_list|(
name|entity
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|validateSuperClassName
argument_list|(
name|entity
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|validateAttributes
argument_list|(
name|entity
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
comment|// validate DbEntity presence
if|if
condition|(
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|entity
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"ObjEntity '%s' has no DbEntity mapping"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|validateClassName
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|String
name|className
init|=
name|entity
operator|.
name|getClassName
argument_list|()
decl_stmt|;
comment|// if mapped to default class, ignore...
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|className
argument_list|)
condition|)
block|{
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
name|invalidCharsInJavaClassName
argument_list|(
name|className
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
name|entity
argument_list|,
literal|"ObjEntity '%s' Java class '%s' contains invalid characters: %s"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|className
argument_list|,
name|invalidChars
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|helper
operator|.
name|invalidDataObjectClass
argument_list|(
name|className
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"Java class '%s' of ObjEntity '%s' is a reserved word"
argument_list|,
name|className
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|className
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|<
literal|0
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"Java class '%s' of ObjEntity '%s' is in a default package"
argument_list|,
name|className
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|validateSuperClassName
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|String
name|superClassName
init|=
name|entity
operator|.
name|getSuperClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|superClassName
argument_list|)
condition|)
block|{
return|return;
comment|// null is Ok
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
name|invalidCharsInJavaClassName
argument_list|(
name|superClassName
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
name|entity
argument_list|,
literal|"ObjEntity '%s' Java superclass '%s' contains invalid characters: %s"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|superClassName
argument_list|,
name|invalidChars
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|helper
operator|.
name|invalidDataObjectClass
argument_list|(
name|superClassName
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"ObjEntity '%s' Java superclass '%s' is a reserved word"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|superClassName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entity
operator|.
name|getDbEntityName
argument_list|()
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|getSuperEntityName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"Sub ObjEntity '%s' has database table declaration different from super ObjEntity '%s'"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|entity
operator|.
name|getSuperEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DataMap
name|map
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return;
block|}
block|}
specifier|private
name|void
name|validateAttributes
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|dbAttributeNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|String
name|dbAttributeName
init|=
name|attribute
operator|.
name|getDbAttribute
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|dbAttributeName
argument_list|)
operator|==
literal|false
condition|)
block|{
if|if
condition|(
name|dbAttributeNames
operator|.
name|contains
argument_list|(
name|dbAttributeName
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"ObjEntity contains duplicate DbAttribute mappings (%s)"
argument_list|,
name|dbAttributeName
argument_list|)
expr_stmt|;
block|}
name|dbAttributeNames
operator|.
name|add
argument_list|(
name|dbAttributeName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|validateName
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ValidationResult
name|validationResult
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
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"Unnamed ObjEntity"
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// check for duplicate names in the parent context
for|for
control|(
name|ObjEntity
name|otherEnt
range|:
name|map
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|otherEnt
operator|==
name|entity
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|otherEnt
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
name|entity
argument_list|,
literal|"Duplicate ObjEntity name: '%s'"
argument_list|,
name|name
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// check for duplicates in other DataMaps
name|DataChannelDescriptor
name|domain
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getDataChannelDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DataMap
name|nextMap
range|:
name|domain
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
if|if
condition|(
name|nextMap
operator|==
name|map
condition|)
block|{
continue|continue;
block|}
name|ObjEntity
name|conflictingEntity
init|=
name|nextMap
operator|.
name|getObjEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|conflictingEntity
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|conflictingEntity
operator|.
name|getClassName
argument_list|()
argument_list|,
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|entity
argument_list|,
literal|"Duplicate ObjEntity name in another DataMap: '%s'"
argument_list|,
name|name
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

