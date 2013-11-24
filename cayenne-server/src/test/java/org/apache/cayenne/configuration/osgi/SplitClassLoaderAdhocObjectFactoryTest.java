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
name|configuration
operator|.
name|osgi
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|SplitClassLoaderAdhocObjectFactoryTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetClassLoader
parameter_list|()
block|{
specifier|final
name|ClassLoader
name|appCl
init|=
name|mock
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClassLoader
name|diCl
init|=
name|mock
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClassLoader
name|serverCl
init|=
name|mock
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|SplitClassLoaderAdhocObjectFactory
name|factory
init|=
operator|new
name|SplitClassLoaderAdhocObjectFactory
argument_list|(
name|appCl
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|ClassLoader
name|cayenneDiClassLoader
parameter_list|()
block|{
return|return
name|diCl
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ClassLoader
name|cayenneServerClassLoader
parameter_list|()
block|{
return|return
name|serverCl
return|;
block|}
block|}
decl_stmt|;
name|assertSame
argument_list|(
name|appCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|appCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|appCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|"org/example/test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|appCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|"/org/example/test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|serverCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|"/org/apache/cayenne/access/DataContext.class"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|diCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|"/org/apache/cayenne/di/Injector.class"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|diCl
argument_list|,
name|factory
operator|.
name|getClassLoader
argument_list|(
literal|"org/apache/cayenne/di/Injector.class"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

