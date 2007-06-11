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
name|access
operator|.
name|jdbc
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
name|Iterator
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
name|ejbql
operator|.
name|EJBQLBaseVisitor
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
name|ejbql
operator|.
name|EJBQLExpression
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
name|DbJoin
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
name|DbRelationship
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
name|ArcProperty
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

begin_comment
comment|/**  * Translator of the EJBQL select clause.  *   * @author Andrus Adamchik  * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLSelectColumnsTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
name|EJBQLSelectColumnsTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitSelectExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitIdentifier
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
specifier|final
name|String
name|idVar
init|=
name|expression
operator|.
name|getText
argument_list|()
decl_stmt|;
comment|// append all table columns
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
name|getCompiledExpression
argument_list|()
operator|.
name|getEntityDescriptor
argument_list|(
name|idVar
argument_list|)
decl_stmt|;
name|PropertyVisitor
name|visitor
init|=
operator|new
name|PropertyVisitor
argument_list|()
block|{
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
name|Iterator
name|dbPathIterator
init|=
name|oa
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathPart
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathPart
operator|instanceof
name|DbRelationship
condition|)
block|{
comment|// DbRelationship rel = (DbRelationship) pathPart;
comment|// dbRelationshipAdded(rel);
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbAttribute
condition|)
block|{
name|DbAttribute
name|dbAttr
init|=
operator|(
name|DbAttribute
operator|)
name|pathPart
decl_stmt|;
if|if
condition|(
name|dbAttr
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"ObjAttribute has no DbAttribute: "
operator|+
name|oa
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|appendColumn
argument_list|(
name|idVar
argument_list|,
name|dbAttr
argument_list|,
name|oa
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|visitRelationship
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|visitRelationship
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|visitRelationship
parameter_list|(
name|ArcProperty
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
name|DbRelationship
name|dbRel
init|=
operator|(
name|DbRelationship
operator|)
name|rel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|joins
init|=
name|dbRel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|joins
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbJoin
name|join
init|=
operator|(
name|DbJoin
operator|)
name|joins
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|src
init|=
name|join
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|appendColumn
argument_list|(
name|idVar
argument_list|,
name|src
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
comment|// EJBQL queries are polimorphic by definition - there is no distinction between
comment|// inheritance/no-inheritance fetch
name|descriptor
operator|.
name|visitAllProperties
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|appendColumn
parameter_list|(
name|String
name|identifier
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|appendColumn
argument_list|(
name|identifier
argument_list|,
name|column
argument_list|,
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendColumn
parameter_list|(
name|String
name|identifier
parameter_list|,
name|DbAttribute
name|column
parameter_list|,
name|String
name|javaType
parameter_list|)
block|{
name|DbEntity
name|table
init|=
operator|(
name|DbEntity
operator|)
name|column
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|context
operator|.
name|createAlias
argument_list|(
name|identifier
argument_list|,
name|table
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|columnName
init|=
name|alias
operator|+
literal|"."
operator|+
name|column
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Set
name|columns
init|=
name|getColumns
argument_list|()
decl_stmt|;
if|if
condition|(
name|columns
operator|.
name|add
argument_list|(
name|columnName
argument_list|)
condition|)
block|{
comment|// using #result directive:
comment|// 1. to ensure that DB default captalization rules won't lead to changing
comment|// result columns capitalization, as #result() gives SQLTemplate a hint as to
comment|// what name is expected by the caller.
comment|// 2. to ensure proper type conversion
name|context
operator|.
name|append
argument_list|(
name|columns
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|?
literal|", "
else|:
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
literal|"#result('"
argument_list|)
operator|.
name|append
argument_list|(
name|columnName
argument_list|)
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|javaType
argument_list|)
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"')"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Set
name|getColumns
parameter_list|()
block|{
name|String
name|columnsKey
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|":columns"
decl_stmt|;
name|Set
name|columns
init|=
operator|(
name|Set
operator|)
name|context
operator|.
name|getTranslationValue
argument_list|(
name|columnsKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|columns
operator|==
literal|null
condition|)
block|{
name|columns
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|context
operator|.
name|putTranslationVariable
argument_list|(
name|columnsKey
argument_list|,
name|columns
argument_list|)
expr_stmt|;
block|}
return|return
name|columns
return|;
block|}
block|}
end_class

end_unit

