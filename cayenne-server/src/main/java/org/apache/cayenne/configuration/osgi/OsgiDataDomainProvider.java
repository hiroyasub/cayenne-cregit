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
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ConfigurationException
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
name|DataDomain
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
name|configuration
operator|.
name|server
operator|.
name|DataDomainProvider
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
name|di
operator|.
name|ClassLoaderManager
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
name|di
operator|.
name|Inject
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
name|EntityResolver
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_comment
comment|// TODO: this is really a hack until we can have fully injectable class loading
end_comment

begin_comment
comment|// at the EntityResolver level per CAY-1887
end_comment

begin_class
specifier|public
class|class
name|OsgiDataDomainProvider
extends|extends
name|DataDomainProvider
block|{
specifier|private
name|ClassLoaderManager
name|classLoaderManager
decl_stmt|;
specifier|public
name|OsgiDataDomainProvider
parameter_list|(
annotation|@
name|Inject
name|ClassLoaderManager
name|classLoaderManager
parameter_list|)
block|{
name|this
operator|.
name|classLoaderManager
operator|=
name|classLoaderManager
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DataDomain
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
comment|// here goes the class loading hack, temporarily setting application
comment|// bundle ClassLoader to be a thread ClassLoader for runtime to start.
name|Thread
name|thread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
name|ClassLoader
name|activeCl
init|=
name|thread
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// using fake package name... as long as it is not
comment|// org.apache.cayenne, this do the right trick
name|thread
operator|.
name|setContextClassLoader
argument_list|(
name|classLoaderManager
operator|.
name|getClassLoader
argument_list|(
literal|"com/"
argument_list|)
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
name|super
operator|.
name|get
argument_list|()
decl_stmt|;
name|EntityResolver
name|entityResolver
init|=
name|domain
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|e
range|:
name|entityResolver
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
comment|// it is not enough to just call 'getObjectClass()' on
comment|// ClassDescriptor - there's an optimization that prevents full
comment|// descriptor resolving... so calling some other method...
name|entityResolver
operator|.
name|getClassDescriptor
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getProperty
argument_list|(
literal|"__dummy__"
argument_list|)
expr_stmt|;
name|entityResolver
operator|.
name|getCallbackRegistry
argument_list|()
expr_stmt|;
block|}
comment|// this triggers callbacks initialization using thread class loader
name|entityResolver
operator|.
name|getCallbackRegistry
argument_list|()
expr_stmt|;
return|return
name|domain
return|;
block|}
finally|finally
block|{
name|thread
operator|.
name|setContextClassLoader
argument_list|(
name|activeCl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

