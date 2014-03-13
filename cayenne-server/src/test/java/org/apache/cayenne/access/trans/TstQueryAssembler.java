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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|access
operator|.
name|DataNode
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
name|Query
import|;
end_import

begin_class
specifier|public
class|class
name|TstQueryAssembler
extends|extends
name|QueryAssembler
block|{
specifier|protected
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRels
decl_stmt|;
specifier|public
name|TstQueryAssembler
parameter_list|(
name|Query
name|q
parameter_list|,
name|DataNode
name|node
parameter_list|,
name|Connection
name|connection
parameter_list|)
throws|throws
name|SQLException
block|{
name|super
argument_list|(
name|q
argument_list|,
name|node
argument_list|,
name|connection
argument_list|)
expr_stmt|;
name|dbRels
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|()
expr_stmt|;
block|}
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
parameter_list|,
name|String
name|joinSplitAlias
parameter_list|)
block|{
name|dbRels
operator|.
name|add
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCurrentAlias
parameter_list|()
block|{
return|return
literal|"ta"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|resetJoinStack
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsTableAliases
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
block|{
return|return
literal|"SELECT * FROM ARTIST"
return|;
block|}
specifier|public
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
specifier|public
name|List
name|getValues
parameter_list|()
block|{
return|return
name|values
return|;
block|}
block|}
end_class

end_unit

