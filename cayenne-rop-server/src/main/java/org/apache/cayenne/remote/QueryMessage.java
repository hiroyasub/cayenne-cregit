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
name|remote
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * A message passed to a DataChannel to request a query execution with result returned as  * QueryResponse.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|QueryMessage
implements|implements
name|ClientMessage
block|{
specifier|protected
name|Query
name|query
decl_stmt|;
comment|// for hessian serialization
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|QueryMessage
parameter_list|()
block|{
block|}
specifier|public
name|QueryMessage
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/**      * Returns a description of the type of message. In this case always "Query".      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Query"
return|;
block|}
block|}
end_class

end_unit

