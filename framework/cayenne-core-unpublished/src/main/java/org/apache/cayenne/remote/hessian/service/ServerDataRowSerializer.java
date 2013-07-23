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
name|remote
operator|.
name|hessian
operator|.
name|service
package|;
end_package

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
name|DataRow
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|AbstractHessianOutput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|AbstractSerializer
import|;
end_import

begin_comment
comment|/**  * A server-side DataRow Hessian serializer.  */
end_comment

begin_class
class|class
name|ServerDataRowSerializer
extends|extends
name|AbstractSerializer
block|{
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|AbstractHessianOutput
name|out
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|out
operator|.
name|addRef
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return;
block|}
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|object
decl_stmt|;
name|out
operator|.
name|writeMapBegin
argument_list|(
name|DataRow
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|row
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeLong
argument_list|(
name|row
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeLong
argument_list|(
name|row
operator|.
name|getReplacesVersion
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|row
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|out
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|writeMapEnd
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
