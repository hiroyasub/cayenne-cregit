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
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

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
name|access
operator|.
name|DataNode
import|;
end_import

begin_comment
comment|/**  * A DataSource wrapper around Cayenne DataNode. Helpful for lazy initialization, i.e.  * when node's DataSource is needed to initialize some other object BEFORE it is  * initialized itself.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|NodeDataSource
implements|implements
name|DataSource
block|{
name|DataNode
name|node
decl_stmt|;
name|NodeDataSource
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
return|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|SQLException
block|{
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|seconds
parameter_list|)
throws|throws
name|SQLException
block|{
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|setLoginTimeout
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getLoginTimeout
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
comment|// JDBC 4 compatibility under Java 1.5
specifier|public
name|boolean
name|isWrapperFor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**      * @since 3.0      */
comment|// JDBC 4 compatibility under Java 1.5
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

