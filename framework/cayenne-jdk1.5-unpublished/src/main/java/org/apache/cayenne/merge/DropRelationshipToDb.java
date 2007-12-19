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
name|merge
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
name|List
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
name|DbAdapter
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
name|DbRelationshipDetected
import|;
end_import

begin_class
specifier|public
class|class
name|DropRelationshipToDb
extends|extends
name|AbstractToDbToken
block|{
specifier|private
name|DbEntity
name|entity
decl_stmt|;
specifier|private
name|DbRelationship
name|rel
decl_stmt|;
specifier|public
name|DropRelationshipToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
name|this
operator|.
name|rel
operator|=
name|rel
expr_stmt|;
block|}
specifier|public
name|String
name|getFkName
parameter_list|()
block|{
if|if
condition|(
name|rel
operator|instanceof
name|DbRelationshipDetected
condition|)
block|{
return|return
operator|(
operator|(
name|DbRelationshipDetected
operator|)
name|rel
operator|)
operator|.
name|getFkName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|String
name|fkName
init|=
name|getFkName
argument_list|()
decl_stmt|;
if|if
condition|(
name|fkName
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|entity
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" DROP CONSTRAINT "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|fkName
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createAddRelationshipToModel
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Drop Relationship"
return|;
block|}
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|s
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"->"
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

