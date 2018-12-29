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
name|exp
operator|.
name|parser
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
name|Cayenne
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
name|ObjectContext
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
name|ObjectId
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
name|access
operator|.
name|DataContext
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
name|Expression
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
name|Entity
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
name|ObjectSelect
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
name|SelectById
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
name|CayenneMapEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|LinkedList
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

begin_comment
comment|/**  * Path expression traversing DB relationships and attributes.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTDbPath
extends|extends
name|ASTPath
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|6623715674339310782L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DB_PREFIX
init|=
literal|"db:"
decl_stmt|;
name|ASTDbPath
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTDbPath
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTDBPATH
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTDbPath
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTDBPATH
argument_list|)
expr_stmt|;
name|setPath
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|evaluateNode
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|o
operator|instanceof
name|Entity
condition|)
block|{
return|return
name|evaluateEntityNode
argument_list|(
operator|(
name|Entity
operator|)
name|o
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|toMap
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|int
name|finalDotIndex
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|String
name|finalPathComponent
init|=
name|finalDotIndex
operator|==
operator|-
literal|1
condition|?
name|path
else|:
name|path
operator|.
name|substring
argument_list|(
name|finalDotIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
operator|(
name|map
operator|!=
literal|null
operator|)
condition|?
name|map
operator|.
name|get
argument_list|(
name|finalPathComponent
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|toMap
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Map
condition|)
block|{
return|return
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
return|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|ObjectId
condition|)
block|{
return|return
operator|(
operator|(
name|ObjectId
operator|)
name|o
operator|)
operator|.
name|getIdSnapshot
argument_list|()
return|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|Persistent
condition|)
block|{
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|o
decl_stmt|;
comment|// before reading full snapshot, check if we can use smaller ID
comment|// snapshot ... it is much cheaper...
return|return
name|persistent
operator|.
name|getObjectContext
argument_list|()
operator|!=
literal|null
condition|?
name|toMap_AttachedObject
argument_list|(
name|persistent
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|persistent
argument_list|)
else|:
name|toMap_DetachedObject
argument_list|(
name|persistent
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|toMap_AttachedObject
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Persistent
name|persistent
parameter_list|)
block|{
return|return
name|path
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>=
literal|0
condition|?
name|toMap_AttchedObject_MultiStepPath
argument_list|(
name|context
argument_list|,
name|persistent
argument_list|)
else|:
name|toMap_AttchedObject_SingleStepPath
argument_list|(
name|context
argument_list|,
name|persistent
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|toMap_AttchedObject_MultiStepPath
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Persistent
name|persistent
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|pathComponents
init|=
name|Cayenne
operator|.
name|getObjEntity
argument_list|(
name|persistent
argument_list|)
operator|.
name|getDbEntity
argument_list|()
operator|.
name|resolvePathComponents
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|LinkedList
argument_list|<
name|DbRelationship
argument_list|>
name|reversedPathComponents
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|pathComponents
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CayenneMapEntry
name|component
init|=
name|pathComponents
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|component
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|rel
init|=
operator|(
name|DbRelationship
operator|)
name|component
decl_stmt|;
name|DbRelationship
name|reverseRelationship
init|=
name|rel
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseRelationship
operator|==
literal|null
condition|)
block|{
name|reverseRelationship
operator|=
name|rel
operator|.
name|createReverseRelationship
argument_list|()
expr_stmt|;
block|}
name|reversedPathComponents
operator|.
name|addFirst
argument_list|(
name|reverseRelationship
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
comment|// an attribute can only occur at the end of the path
block|}
block|}
name|DbEntity
name|finalEntity
init|=
name|reversedPathComponents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|StringBuilder
name|reversedPathStr
init|=
operator|new
name|StringBuilder
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
name|reversedPathComponents
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|reversedPathStr
operator|.
name|append
argument_list|(
name|reversedPathComponents
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|reversedPathComponents
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|reversedPathStr
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ObjectSelect
operator|.
name|dbQuery
argument_list|(
name|finalEntity
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
name|reversedPathStr
operator|.
name|toString
argument_list|()
argument_list|,
name|persistent
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|toMap_AttchedObject_SingleStepPath
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Persistent
name|persistent
parameter_list|)
block|{
name|ObjectId
name|oid
init|=
name|persistent
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// TODO: snapshotting API should not be limited to DataContext...
if|if
condition|(
name|context
operator|instanceof
name|DataContext
condition|)
block|{
return|return
operator|(
operator|(
name|DataContext
operator|)
name|context
operator|)
operator|.
name|currentSnapshot
argument_list|(
name|persistent
argument_list|)
return|;
block|}
if|if
condition|(
name|oid
operator|!=
literal|null
condition|)
block|{
return|return
name|SelectById
operator|.
name|dataRowQuery
argument_list|(
name|persistent
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
return|;
block|}
comment|// fallback to ID snapshot as a last resort
return|return
name|toMap_DetachedObject
argument_list|(
name|persistent
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|toMap_DetachedObject
parameter_list|(
name|Persistent
name|persistent
parameter_list|)
block|{
name|ObjectId
name|oid
init|=
name|persistent
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// returning null here is for backwards compatibility. Should we throw
comment|// instead?
return|return
operator|(
name|oid
operator|!=
literal|null
operator|)
condition|?
name|oid
operator|.
name|getIdSnapshot
argument_list|()
else|:
literal|null
return|;
block|}
comment|/** 	 * Creates a copy of this expression node, without copying children. 	 */
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
name|ASTDbPath
name|copy
init|=
operator|new
name|ASTDbPath
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|copy
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|copy
operator|.
name|setPathAliases
argument_list|(
name|pathAliases
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
comment|// warning: non-standard EJBQL...
name|out
operator|.
name|append
argument_list|(
name|DB_PREFIX
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|rootId
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|void
name|appendAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|DB_PREFIX
argument_list|)
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|Expression
operator|.
name|DB_PATH
return|;
block|}
block|}
end_class

end_unit

