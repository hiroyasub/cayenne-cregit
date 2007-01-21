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
name|art
operator|.
name|oneway
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
name|access
operator|.
name|event
operator|.
name|DataContextEvent
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
name|event
operator|.
name|DataObjectTransactionEventListener
import|;
end_import

begin_class
specifier|public
class|class
name|Artist
extends|extends
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|oneway
operator|.
name|auto
operator|.
name|_Artist
implements|implements
name|DataObjectTransactionEventListener
block|{
specifier|private
name|boolean
name|_receivedWillCommit
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|_receivedDidCommit
init|=
literal|false
decl_stmt|;
specifier|protected
name|String
name|someOtherProperty
decl_stmt|;
specifier|protected
name|Object
name|someOtherObjectProperty
decl_stmt|;
specifier|public
name|Artist
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|didCommit
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
block|{
name|_receivedDidCommit
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|willCommit
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
block|{
name|_receivedWillCommit
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|receivedDidCommit
parameter_list|()
block|{
return|return
name|_receivedDidCommit
return|;
block|}
specifier|public
name|boolean
name|receivedWillCommit
parameter_list|()
block|{
return|return
name|_receivedWillCommit
return|;
block|}
specifier|public
name|void
name|resetEvents
parameter_list|()
block|{
name|_receivedWillCommit
operator|=
literal|false
expr_stmt|;
name|_receivedDidCommit
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|String
name|getSomeOtherProperty
parameter_list|()
block|{
return|return
name|someOtherProperty
return|;
block|}
specifier|public
name|void
name|setSomeOtherProperty
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|someOtherProperty
operator|=
name|string
expr_stmt|;
block|}
specifier|public
name|Object
name|getSomeOtherObjectProperty
parameter_list|()
block|{
return|return
name|someOtherObjectProperty
return|;
block|}
specifier|public
name|void
name|setSomeOtherObjectProperty
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|someOtherObjectProperty
operator|=
name|object
expr_stmt|;
block|}
block|}
end_class

end_unit

