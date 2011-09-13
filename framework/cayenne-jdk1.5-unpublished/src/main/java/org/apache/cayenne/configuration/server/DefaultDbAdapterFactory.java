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
name|server
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
name|DatabaseMetaData
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|AutoAdapter
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
name|dba
operator|.
name|JdbcAdapter
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
name|AdhocObjectFactory
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
name|di
operator|.
name|Injector
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
name|Provider
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
name|log
operator|.
name|JdbcEventLogger
import|;
end_import

begin_comment
comment|/**  * A factory of DbAdapters that either loads user-provided adapter or guesses the adapter  * type from the database metadata.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDbAdapterFactory
implements|implements
name|DbAdapterFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DETECTORS_LIST
init|=
literal|"org.apache.cayenne.configuration.server.DefaultDbAdapterFactory.detectors"
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|JdbcEventLogger
name|jdbcEventLogger
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbAdapterDetector
argument_list|>
name|detectors
decl_stmt|;
specifier|public
name|DefaultDbAdapterFactory
parameter_list|(
annotation|@
name|Inject
argument_list|(
name|DETECTORS_LIST
argument_list|)
name|List
argument_list|<
name|DbAdapterDetector
argument_list|>
name|detectors
parameter_list|)
block|{
if|if
condition|(
name|detectors
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null detectors list"
argument_list|)
throw|;
block|}
name|this
operator|.
name|detectors
operator|=
name|detectors
expr_stmt|;
block|}
specifier|public
name|DbAdapter
name|createAdapter
parameter_list|(
specifier|final
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|,
specifier|final
name|DataSource
name|dataSource
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|adapterType
init|=
name|nodeDescriptor
operator|.
name|getAdapterType
argument_list|()
decl_stmt|;
if|if
condition|(
name|adapterType
operator|!=
literal|null
condition|)
block|{
return|return
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|adapterType
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|AutoAdapter
argument_list|(
operator|new
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
argument_list|()
block|{
specifier|public
name|DbAdapter
name|get
parameter_list|()
block|{
return|return
name|detectAdapter
argument_list|(
name|nodeDescriptor
argument_list|,
name|dataSource
argument_list|)
return|;
block|}
block|}
argument_list|,
name|jdbcEventLogger
argument_list|)
return|;
block|}
block|}
specifier|protected
name|DbAdapter
name|detectAdapter
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|,
name|DataSource
name|dataSource
parameter_list|)
block|{
if|if
condition|(
name|detectors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|defaultAdapter
argument_list|()
return|;
block|}
try|try
block|{
name|Connection
name|c
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|detectAdapter
argument_list|(
name|c
operator|.
name|getMetaData
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
try|try
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// ignore...
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error detecting database type: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|DbAdapter
name|detectAdapter
parameter_list|(
name|DatabaseMetaData
name|metaData
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// iterate in reverse order to allow custom factories to take precedence over the
comment|// default ones configured in constructor
for|for
control|(
name|int
name|i
init|=
name|detectors
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|DbAdapterDetector
name|detector
init|=
name|detectors
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|detector
operator|.
name|createAdapter
argument_list|(
name|metaData
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|jdbcEventLogger
operator|.
name|log
argument_list|(
literal|"Detected and installed adapter: "
operator|+
name|adapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: should detector do this??
name|injector
operator|.
name|injectMembers
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
return|return
name|adapter
return|;
block|}
block|}
return|return
name|defaultAdapter
argument_list|()
return|;
block|}
specifier|protected
name|DbAdapter
name|defaultAdapter
parameter_list|()
block|{
name|jdbcEventLogger
operator|.
name|log
argument_list|(
literal|"Failed to detect database type, using generic adapter"
argument_list|)
expr_stmt|;
return|return
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

