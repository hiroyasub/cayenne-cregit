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
name|service
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|query
operator|.
name|Query
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
name|remote
operator|.
name|QueryMessage
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
name|remote
operator|.
name|RemoteSession
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
name|Util
import|;
end_import

begin_class
specifier|public
class|class
name|BaseRemoteServiceTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testProcessMessageExceptionSerializability
parameter_list|()
throws|throws
name|Throwable
block|{
name|BaseRemoteService
name|handler
init|=
operator|new
name|BaseRemoteService
argument_list|()
block|{
specifier|protected
name|ServerSession
name|createServerSession
parameter_list|()
block|{
return|return
operator|new
name|ServerSession
argument_list|(
operator|new
name|RemoteSession
argument_list|(
literal|"a"
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|ServerSession
name|createServerSession
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|createServerSession
argument_list|()
return|;
block|}
specifier|protected
name|ServerSession
name|getServerSession
parameter_list|()
block|{
return|return
name|createServerSession
argument_list|()
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|processMessage
argument_list|(
operator|new
name|QueryMessage
argument_list|(
literal|null
argument_list|)
block|{
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
comment|// serializable exception thrown
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|()
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|handler
operator|.
name|processMessage
argument_list|(
operator|new
name|QueryMessage
argument_list|(
literal|null
argument_list|)
block|{
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
comment|// non-serializable exception thrown
throw|throw
operator|new
name|MockUnserializableException
argument_list|()
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

