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
name|access
operator|.
name|translator
operator|.
name|select
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|EntityResult
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|reflect
operator|.
name|PropertyVisitor
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
name|reflect
operator|.
name|ToManyProperty
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
name|reflect
operator|.
name|ToOneProperty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|sqlbuilder
operator|.
name|SQLBuilder
operator|.
name|table
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|DescriptorColumnExtractor
extends|extends
name|BaseColumnExtractor
implements|implements
name|PropertyVisitor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PREFETCH_PREFIX
init|=
literal|"p:"
decl_stmt|;
specifier|private
specifier|final
name|ClassDescriptor
name|descriptor
decl_stmt|;
specifier|private
specifier|final
name|PathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|columnTracker
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|EntityResult
name|entityResult
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|private
name|String
name|labelPrefix
decl_stmt|;
name|DescriptorColumnExtractor
parameter_list|(
name|TranslatorContext
name|context
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|pathTranslator
operator|=
name|context
operator|.
name|getPathTranslator
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|extract
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
name|boolean
name|newEntityResult
init|=
literal|false
decl_stmt|;
name|this
operator|.
name|labelPrefix
operator|=
literal|null
expr_stmt|;
name|TranslatorContext
operator|.
name|DescriptorType
name|type
init|=
name|TranslatorContext
operator|.
name|DescriptorType
operator|.
name|OTHER
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
name|prefix
operator|.
name|startsWith
argument_list|(
name|PREFETCH_PREFIX
argument_list|)
condition|)
block|{
name|type
operator|=
name|TranslatorContext
operator|.
name|DescriptorType
operator|.
name|PREFETCH
expr_stmt|;
name|labelPrefix
operator|=
name|prefix
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|needsResultSetMapping
argument_list|()
condition|)
block|{
name|entityResult
operator|=
name|context
operator|.
name|getRootEntityResult
argument_list|()
expr_stmt|;
if|if
condition|(
name|entityResult
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't process prefetch descriptor without root."
argument_list|)
throw|;
block|}
name|newEntityResult
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|needsResultSetMapping
argument_list|()
condition|)
block|{
name|entityResult
operator|=
operator|new
name|EntityResult
argument_list|(
name|descriptor
operator|.
name|getObjectClass
argument_list|()
argument_list|)
expr_stmt|;
name|newEntityResult
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
operator|==
name|context
operator|.
name|getRootDbEntity
argument_list|()
condition|)
block|{
name|type
operator|=
name|TranslatorContext
operator|.
name|DescriptorType
operator|.
name|ROOT
expr_stmt|;
name|context
operator|.
name|setRootEntityResult
argument_list|(
name|entityResult
argument_list|)
expr_stmt|;
block|}
block|}
name|context
operator|.
name|markDescriptorStart
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|visitAllProperties
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// add remaining needed attrs from DbEntity
name|DbEntity
name|table
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForPath
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
for|for
control|(
name|DbAttribute
name|dba
range|:
name|table
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|String
name|columnUniqueName
init|=
name|alias
operator|+
literal|'.'
operator|+
name|dba
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|columnTracker
operator|.
name|add
argument_list|(
name|columnUniqueName
argument_list|)
condition|)
block|{
name|addDbAttribute
argument_list|(
name|prefix
argument_list|,
name|labelPrefix
argument_list|,
name|dba
argument_list|)
expr_stmt|;
name|addEntityResultField
argument_list|(
name|dba
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newEntityResult
condition|)
block|{
name|context
operator|.
name|getSqlResult
argument_list|()
operator|.
name|addEntityResult
argument_list|(
name|entityResult
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|markDescriptorEnd
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|ObjAttribute
name|oa
init|=
name|property
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|oa
operator|.
name|isLazy
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|PathTranslationResult
name|result
init|=
name|pathTranslator
operator|.
name|translatePath
argument_list|(
name|oa
operator|.
name|getEntity
argument_list|()
argument_list|,
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|size
argument_list|()
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|ResultNodeDescriptor
name|resultNodeDescriptor
init|=
name|processTranslationResult
argument_list|(
name|result
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultNodeDescriptor
operator|!=
literal|null
condition|)
block|{
name|resultNodeDescriptor
operator|.
name|setJavaType
argument_list|(
name|oa
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
name|DbAttribute
name|dbAttribute
init|=
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|dataRowKey
init|=
name|result
operator|.
name|getAttributePaths
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|+
literal|"."
operator|+
name|dbAttribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|resultNodeDescriptor
operator|.
name|setDataRowKey
argument_list|(
name|dataRowKey
argument_list|)
expr_stmt|;
name|addEntityResultField
argument_list|(
name|dataRowKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addEntityResultField
argument_list|(
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|ObjRelationship
name|rel
init|=
name|property
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|rel
operator|.
name|isToPK
argument_list|()
condition|)
block|{
comment|// should ignore toOne not on PK relationship as it doesn't have any column to add to result
return|return
literal|true
return|;
block|}
name|PathTranslationResult
name|result
init|=
name|pathTranslator
operator|.
name|translatePath
argument_list|(
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|size
argument_list|()
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|processTranslationResult
argument_list|(
name|result
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|addEntityResultField
argument_list|(
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|ResultNodeDescriptor
name|processTranslationResult
parameter_list|(
name|PathTranslationResult
name|result
parameter_list|,
name|int
name|i
parameter_list|)
block|{
name|String
name|path
init|=
name|result
operator|.
name|getAttributePaths
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|alias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|columnUniqueName
init|=
name|alias
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|columnTracker
operator|.
name|add
argument_list|(
name|columnUniqueName
argument_list|)
condition|)
block|{
name|String
name|columnLabelPrefix
init|=
name|path
decl_stmt|;
if|if
condition|(
name|columnLabelPrefix
operator|.
name|length
argument_list|()
operator|>
literal|2
condition|)
block|{
if|if
condition|(
name|columnLabelPrefix
operator|.
name|startsWith
argument_list|(
name|PREFETCH_PREFIX
argument_list|)
condition|)
block|{
name|columnLabelPrefix
operator|=
name|columnLabelPrefix
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|attributeName
init|=
name|columnLabelPrefix
operator|.
name|isEmpty
argument_list|()
condition|?
name|attribute
operator|.
name|getName
argument_list|()
else|:
name|columnLabelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Node
name|columnNode
init|=
name|table
argument_list|(
name|alias
argument_list|)
operator|.
name|column
argument_list|(
name|attribute
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|context
operator|.
name|addResultNode
argument_list|(
name|columnNode
argument_list|,
name|attributeName
argument_list|)
operator|.
name|setDbAttribute
argument_list|(
name|attribute
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|addEntityResultField
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|String
name|name
init|=
name|labelPrefix
operator|==
literal|null
condition|?
name|attribute
operator|.
name|getName
argument_list|()
else|:
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|needsResultSetMapping
argument_list|()
condition|)
block|{
name|entityResult
operator|.
name|addDbField
argument_list|(
name|name
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addEntityResultField
parameter_list|(
name|String
name|nameForFlattenedAttribute
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|needsResultSetMapping
argument_list|()
condition|)
block|{
name|entityResult
operator|.
name|addDbField
argument_list|(
name|nameForFlattenedAttribute
argument_list|,
name|nameForFlattenedAttribute
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

