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
name|Collections
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

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataObjectMatchTranslator
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DbAttribute
argument_list|>
name|attributes
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
decl_stmt|;
specifier|protected
name|String
name|operation
decl_stmt|;
specifier|protected
name|Expression
name|expression
decl_stmt|;
specifier|protected
name|DbRelationship
name|relationship
decl_stmt|;
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|attributes
operator|=
literal|null
expr_stmt|;
name|values
operator|=
literal|null
expr_stmt|;
name|operation
operator|=
literal|null
expr_stmt|;
name|expression
operator|=
literal|null
expr_stmt|;
name|relationship
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Initializes itself to do translation of the match ending       * with a DbRelationship.      */
specifier|public
name|void
name|setRelationship
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
name|this
operator|.
name|relationship
operator|=
name|rel
expr_stmt|;
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DbAttribute
argument_list|>
argument_list|(
name|rel
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|rel
operator|.
name|isToMany
argument_list|()
operator|||
operator|!
name|rel
operator|.
name|isToPK
argument_list|()
condition|)
block|{
comment|// match on target PK
name|DbEntity
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
comment|// index by name
for|for
control|(
name|DbAttribute
name|pkAttr
range|:
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|pkAttr
operator|.
name|getName
argument_list|()
argument_list|,
name|pkAttr
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// match on this FK
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
comment|// index by target name
name|attributes
operator|.
name|put
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|join
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setDataObject
parameter_list|(
name|Persistent
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|values
operator|=
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
return|return;
block|}
name|setObjectId
argument_list|(
name|obj
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setObjectId
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null ObjectId, probably an attempt to use TRANSIENT object as a query parameter."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Temporary id, probably an attempt to use NEW object as a query parameter."
argument_list|)
throw|;
block|}
else|else
block|{
name|values
operator|=
name|id
operator|.
name|getIdSnapshot
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|()
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"An attempt to use uninitialized DataObjectMatchTranslator: "
operator|+
literal|"[attributes: null, values: "
operator|+
name|values
operator|+
literal|"]"
argument_list|)
throw|;
block|}
return|return
name|attributes
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|DbRelationship
name|getRelationship
parameter_list|()
block|{
return|return
name|relationship
return|;
block|}
specifier|public
name|DbAttribute
name|getAttribute
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|values
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
block|}
end_class

end_unit

