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
name|trans
package|;
end_package

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
name|JoinType
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
name|UpdateQuery
import|;
end_import

begin_comment
comment|/**  * Class implements default translation mechanism of org.apache.cayenne.query.UpdateQuery  * objects to SQL UPDATE statements.  *   * @author Andrus Adamchik  * @deprecated since 3.0 use EJBQL or SQLTemplate  */
end_comment

begin_class
specifier|public
class|class
name|UpdateTranslator
extends|extends
name|QueryAssembler
block|{
annotation|@
name|Override
specifier|public
name|void
name|dbRelationshipAdded
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|JoinType
name|joinType
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"db relationships not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCurrentAlias
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"aliases not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|resetJoinStack
parameter_list|()
block|{
comment|// noop - path processing is not supported.
block|}
comment|/** Method that converts an update query into SQL string */
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|queryBuf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
literal|"UPDATE "
argument_list|)
expr_stmt|;
comment|// 1. append table name
name|DbEntity
name|dbEnt
init|=
name|getRootEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
name|dbEnt
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2. build "set ..." clause
name|buildSetClause
argument_list|(
name|queryBuf
argument_list|,
operator|(
name|UpdateQuery
operator|)
name|query
argument_list|)
expr_stmt|;
comment|// 3. build qualifier
name|String
name|qualifierStr
init|=
name|adapter
operator|.
name|getQualifierTranslator
argument_list|(
name|this
argument_list|)
operator|.
name|doTranslation
argument_list|()
decl_stmt|;
if|if
condition|(
name|qualifierStr
operator|!=
literal|null
condition|)
name|queryBuf
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
operator|.
name|append
argument_list|(
name|qualifierStr
argument_list|)
expr_stmt|;
return|return
name|queryBuf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Translate updated values and relationships into "SET ATTR1 = Val1, ..." SQL      * statement.      */
specifier|private
name|void
name|buildSetClause
parameter_list|(
name|StringBuffer
name|queryBuf
parameter_list|,
name|UpdateQuery
name|query
parameter_list|)
block|{
name|Map
name|updAttrs
init|=
name|query
operator|.
name|getUpdAttributes
argument_list|()
decl_stmt|;
comment|// set of keys.. each key is supposed to be ObjAttribute
name|Iterator
name|attrIt
init|=
name|updAttrs
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|attrIt
operator|.
name|hasNext
argument_list|()
condition|)
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Nothing to update."
argument_list|)
throw|;
name|DbEntity
name|dbEnt
init|=
name|getRootEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
literal|" SET "
argument_list|)
expr_stmt|;
comment|// append updated attribute values
name|boolean
name|appendedSomething
init|=
literal|false
decl_stmt|;
comment|// now process other attrs in the loop
while|while
condition|(
name|attrIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|attrIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|nextKey
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|attrVal
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|appendedSomething
condition|)
name|queryBuf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
name|nextKey
argument_list|)
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
name|super
operator|.
name|addToParamList
argument_list|(
operator|(
name|DbAttribute
operator|)
name|dbEnt
operator|.
name|getAttribute
argument_list|(
name|nextKey
argument_list|)
argument_list|,
name|attrVal
argument_list|)
expr_stmt|;
name|appendedSomething
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

