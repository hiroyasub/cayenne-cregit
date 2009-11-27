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
name|ObjectContext
import|;
end_import

begin_comment
comment|/**  * Test for entites that are implemented in same class on client and server  */
end_comment

begin_class
specifier|public
class|class
name|PersistentCase
extends|extends
name|RemoteCayenneCase
block|{
name|boolean
name|server
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|runBare
parameter_list|()
throws|throws
name|Throwable
block|{
name|server
operator|=
literal|true
expr_stmt|;
name|super
operator|.
name|runBare
argument_list|()
expr_stmt|;
name|server
operator|=
literal|false
expr_stmt|;
comment|//testing ROP with all serialozation policies
name|runBareSimple
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ObjectContext
name|createContext
parameter_list|()
block|{
if|if
condition|(
name|server
condition|)
block|{
return|return
name|createDataContext
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|createROPContext
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

