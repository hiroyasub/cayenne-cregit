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
name|tx
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

begin_comment
comment|/**  * Created by andrus on 9/19/16.  */
end_comment

begin_class
class|class
name|DoNothingTransactionListener
implements|implements
name|TransactionListener
block|{
specifier|private
specifier|static
name|TransactionListener
name|INSTANCE
init|=
operator|new
name|DoNothingTransactionListener
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|TransactionListener
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|DoNothingTransactionListener
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|willCommit
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
comment|// do nothing...
block|}
annotation|@
name|Override
specifier|public
name|void
name|willRollback
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
comment|// do nothing...
block|}
annotation|@
name|Override
specifier|public
name|void
name|willAddConnection
parameter_list|(
name|Transaction
name|tx
parameter_list|,
name|String
name|connectionName
parameter_list|,
name|Connection
name|connection
parameter_list|)
block|{
comment|// do nothing...
block|}
block|}
end_class

end_unit

