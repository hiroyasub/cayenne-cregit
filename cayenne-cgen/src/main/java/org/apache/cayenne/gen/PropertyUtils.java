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
name|util
operator|.
name|Arrays
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
name|List
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Persistent
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
name|dba
operator|.
name|TypesMapping
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
name|ExpressionFactory
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
name|property
operator|.
name|BaseProperty
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
name|property
operator|.
name|DateProperty
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
name|property
operator|.
name|EntityProperty
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
name|property
operator|.
name|ListProperty
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
name|property
operator|.
name|MapProperty
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
name|property
operator|.
name|NumericProperty
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
name|property
operator|.
name|PropertyFactory
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
name|property
operator|.
name|SetProperty
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
name|property
operator|.
name|StringProperty
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
name|DbEntity
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
name|ObjRelationship
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|PropertyUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PK_PROPERTY_SUFFIX
init|=
literal|"_PK_PROPERTY"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|FACTORY_METHODS
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|BaseProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createBase"
argument_list|)
expr_stmt|;
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|NumericProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createNumeric"
argument_list|)
expr_stmt|;
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|StringProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createString"
argument_list|)
expr_stmt|;
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|DateProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createDate"
argument_list|)
expr_stmt|;
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|ListProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createList"
argument_list|)
expr_stmt|;
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|SetProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createSet"
argument_list|)
expr_stmt|;
name|FACTORY_METHODS
operator|.
name|put
argument_list|(
name|MapProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"createMap"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|JAVA_DATE_TYPES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|java
operator|.
name|util
operator|.
name|Date
operator|.
name|class
argument_list|,
name|java
operator|.
name|time
operator|.
name|LocalDate
operator|.
name|class
argument_list|,
name|java
operator|.
name|time
operator|.
name|LocalTime
operator|.
name|class
argument_list|,
name|java
operator|.
name|time
operator|.
name|LocalDateTime
operator|.
name|class
argument_list|,
name|java
operator|.
name|sql
operator|.
name|Date
operator|.
name|class
argument_list|,
name|java
operator|.
name|sql
operator|.
name|Time
operator|.
name|class
argument_list|,
name|java
operator|.
name|sql
operator|.
name|Timestamp
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ImportUtils
name|importUtils
decl_stmt|;
specifier|public
name|PropertyUtils
parameter_list|(
name|ImportUtils
name|importUtils
parameter_list|)
block|{
name|this
operator|.
name|importUtils
operator|=
name|importUtils
expr_stmt|;
block|}
specifier|public
name|void
name|addImportForPK
parameter_list|(
name|EntityUtils
name|entityUtils
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|DbEntity
name|entity
init|=
name|entityUtils
operator|.
name|objEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|boolean
name|needToCreatePK
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|entityUtils
operator|.
name|declaresDbAttribute
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|getPropertyTypeForAttribute
argument_list|(
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
name|needToCreatePK
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|needToCreatePK
condition|)
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|PropertyFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|ExpressionFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addImport
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|PropertyFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|getPropertyTypeForAttribute
argument_list|(
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addImport
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|PropertyFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
operator|!=
literal|null
operator|&&
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|Persistent
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|importUtils
operator|.
name|addType
argument_list|(
name|getPropertyTypeForJavaClass
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|importUtils
operator|.
name|addType
argument_list|(
name|relationship
operator|.
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|propertyDefinition
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|StringUtils
name|utils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|attributeType
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|propertyType
init|=
name|getPropertyTypeForAttribute
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
name|String
name|propertyFactoryMethod
init|=
name|factoryMethodForPropertyType
argument_list|(
name|propertyType
argument_list|)
decl_stmt|;
name|attributeType
operator|=
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|attributeType
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"public static final %s<%s> %s = PropertyFactory.%s(ExpressionFactory.dbPathExp(\"%s\"), %s.class);"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|propertyType
argument_list|)
argument_list|,
name|attributeType
argument_list|,
name|utils
operator|.
name|capitalizedAsConstant
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
operator|+
name|PK_PROPERTY_SUFFIX
argument_list|)
argument_list|,
name|propertyFactoryMethod
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeType
argument_list|)
return|;
block|}
specifier|private
name|String
name|getPropertyTypeForAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|String
name|attributeType
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|TypesMapping
operator|.
name|JAVA_BYTES
operator|.
name|equals
argument_list|(
name|attributeType
argument_list|)
condition|)
block|{
return|return
name|BaseProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|javaClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|attributeType
argument_list|)
decl_stmt|;
if|if
condition|(
name|Number
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|NumericProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|CharSequence
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|StringProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|JAVA_DATE_TYPES
operator|.
name|contains
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|DateProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|BaseProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|propertyDefinition
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|StringUtils
name|utils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|propertyType
init|=
name|getPropertyTypeForAttribute
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
name|String
name|propertyFactoryMethod
init|=
name|factoryMethodForPropertyType
argument_list|(
name|propertyType
argument_list|)
decl_stmt|;
name|String
name|attributeType
init|=
name|utils
operator|.
name|stripGeneric
argument_list|(
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"public static final %s<%s> %s = PropertyFactory.%s(\"%s\", %s.class);"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|propertyType
argument_list|)
argument_list|,
name|attributeType
argument_list|,
name|utils
operator|.
name|capitalizedAsConstant
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|propertyFactoryMethod
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeType
argument_list|)
return|;
block|}
specifier|public
name|String
name|propertyDefinition
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
return|return
name|toManyRelationshipDefinition
argument_list|(
name|relationship
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toOneRelationshipDefinition
argument_list|(
name|relationship
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|toManyRelationshipDefinition
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|Map
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|relationship
operator|.
name|getCollectionType
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|mapRelationshipDefinition
argument_list|(
name|relationship
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|collectionRelationshipDefinition
argument_list|(
name|relationship
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|mapRelationshipDefinition
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|StringUtils
name|utils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|propertyType
init|=
name|getPropertyTypeForJavaClass
argument_list|(
name|relationship
argument_list|)
decl_stmt|;
name|String
name|propertyFactoryMethod
init|=
name|factoryMethodForPropertyType
argument_list|(
name|propertyType
argument_list|)
decl_stmt|;
name|String
name|mapKeyType
init|=
name|EntityUtils
operator|.
name|getMapKeyTypeInternal
argument_list|(
name|relationship
argument_list|)
decl_stmt|;
name|String
name|attributeType
init|=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|?
name|Persistent
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
else|:
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getClassName
argument_list|()
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"public static final %s<%s, %s> %s = PropertyFactory.%s(\"%s\", %s.class, %s.class);"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|propertyType
argument_list|)
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|mapKeyType
argument_list|)
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|attributeType
argument_list|)
argument_list|,
name|utils
operator|.
name|capitalizedAsConstant
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|propertyFactoryMethod
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|mapKeyType
argument_list|)
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|attributeType
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|collectionRelationshipDefinition
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|StringUtils
name|utils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|propertyType
init|=
name|getPropertyTypeForJavaClass
argument_list|(
name|relationship
argument_list|)
decl_stmt|;
name|String
name|propertyFactoryMethod
init|=
name|factoryMethodForPropertyType
argument_list|(
name|propertyType
argument_list|)
decl_stmt|;
name|String
name|entityType
init|=
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|?
name|Persistent
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
else|:
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"public static final %s<%s> %s = PropertyFactory.%s(\"%s\", %s.class);"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|propertyType
argument_list|)
argument_list|,
name|entityType
argument_list|,
name|utils
operator|.
name|capitalizedAsConstant
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|propertyFactoryMethod
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|entityType
argument_list|)
return|;
block|}
specifier|private
name|String
name|toOneRelationshipDefinition
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|StringUtils
name|utils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|propertyType
init|=
name|EntityProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|propertyFactoryMethod
init|=
literal|"createEntity"
decl_stmt|;
name|String
name|attributeType
init|=
operator|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
operator|==
literal|null
operator|||
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
operator|)
condition|?
name|Persistent
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
else|:
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"public static final %s<%s> %s = PropertyFactory.%s(\"%s\", %s.class);"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|propertyType
argument_list|)
argument_list|,
name|attributeType
argument_list|,
name|utils
operator|.
name|capitalizedAsConstant
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|propertyFactoryMethod
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeType
argument_list|)
return|;
block|}
specifier|private
name|String
name|factoryMethodForPropertyType
parameter_list|(
name|String
name|propertyType
parameter_list|)
block|{
return|return
name|FACTORY_METHODS
operator|.
name|get
argument_list|(
name|propertyType
argument_list|)
return|;
block|}
specifier|private
name|String
name|getPropertyTypeForJavaClass
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|String
name|collectionType
init|=
name|relationship
operator|.
name|getCollectionType
argument_list|()
decl_stmt|;
if|if
condition|(
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
condition|)
block|{
return|return
name|MapProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|java
operator|.
name|util
operator|.
name|List
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
condition|)
block|{
return|return
name|ListProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|SetProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|EntityProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|String
name|getPropertyTypeForAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|String
name|attributeType
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|importUtils
operator|.
name|isNumericPrimitive
argument_list|(
name|attributeType
argument_list|)
condition|)
block|{
return|return
name|NumericProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|javaClass
decl_stmt|;
if|if
condition|(
name|importUtils
operator|.
name|isPrimitive
argument_list|(
name|attributeType
argument_list|)
operator|||
name|attributeType
operator|.
name|startsWith
argument_list|(
literal|"java."
argument_list|)
condition|)
block|{
name|javaClass
operator|=
name|attribute
operator|.
name|getJavaClass
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return
name|BaseProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|Number
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|NumericProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|CharSequence
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|StringProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|JAVA_DATE_TYPES
operator|.
name|contains
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|DateProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|BaseProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

