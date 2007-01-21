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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
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
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
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
name|sql
operator|.
name|SQLWarning
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Savepoint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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

begin_comment
comment|/**  * A wrapper of a JDBC connection that is attached to a transaction. The behavior of this  * object to delegate all method calls to the underlying connection, except for the  * 'close' method that is implemented as noop in hope that a transaction originator will  * close the underlying Connection object.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|TransactionConnectionDecorator
implements|implements
name|Connection
block|{
name|Connection
name|connection
decl_stmt|;
name|TransactionConnectionDecorator
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
comment|// the only method that is NOT delegated...
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// noop
block|}
specifier|public
name|void
name|clearWarnings
parameter_list|()
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|clearWarnings
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|commit
parameter_list|()
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Statement
name|createStatement
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|createStatement
argument_list|()
return|;
block|}
specifier|public
name|Statement
name|createStatement
parameter_list|(
name|int
name|resultSetType
parameter_list|,
name|int
name|resultSetConcurrency
parameter_list|,
name|int
name|resultSetHoldability
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|createStatement
argument_list|(
name|resultSetType
argument_list|,
name|resultSetConcurrency
argument_list|,
name|resultSetHoldability
argument_list|)
return|;
block|}
specifier|public
name|Statement
name|createStatement
parameter_list|(
name|int
name|resultSetType
parameter_list|,
name|int
name|resultSetConcurrency
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|createStatement
argument_list|(
name|resultSetType
argument_list|,
name|resultSetConcurrency
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getAutoCommit
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getAutoCommit
argument_list|()
return|;
block|}
specifier|public
name|String
name|getCatalog
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getCatalog
argument_list|()
return|;
block|}
specifier|public
name|int
name|getHoldability
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getHoldability
argument_list|()
return|;
block|}
specifier|public
name|DatabaseMetaData
name|getMetaData
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getMetaData
argument_list|()
return|;
block|}
specifier|public
name|int
name|getTransactionIsolation
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getTransactionIsolation
argument_list|()
return|;
block|}
specifier|public
name|Map
name|getTypeMap
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getTypeMap
argument_list|()
return|;
block|}
specifier|public
name|SQLWarning
name|getWarnings
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|getWarnings
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isClosed
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|isClosed
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|isReadOnly
argument_list|()
return|;
block|}
specifier|public
name|String
name|nativeSQL
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|nativeSQL
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|CallableStatement
name|prepareCall
parameter_list|(
name|String
name|sql
parameter_list|,
name|int
name|resultSetType
parameter_list|,
name|int
name|resultSetConcurrency
parameter_list|,
name|int
name|resultSetHoldability
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareCall
argument_list|(
name|sql
argument_list|,
name|resultSetType
argument_list|,
name|resultSetConcurrency
argument_list|,
name|resultSetHoldability
argument_list|)
return|;
block|}
specifier|public
name|CallableStatement
name|prepareCall
parameter_list|(
name|String
name|sql
parameter_list|,
name|int
name|resultSetType
parameter_list|,
name|int
name|resultSetConcurrency
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareCall
argument_list|(
name|sql
argument_list|,
name|resultSetType
argument_list|,
name|resultSetConcurrency
argument_list|)
return|;
block|}
specifier|public
name|CallableStatement
name|prepareCall
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareCall
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|int
name|resultSetType
parameter_list|,
name|int
name|resultSetConcurrency
parameter_list|,
name|int
name|resultSetHoldability
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|,
name|resultSetType
argument_list|,
name|resultSetConcurrency
argument_list|,
name|resultSetHoldability
argument_list|)
return|;
block|}
specifier|public
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|int
name|resultSetType
parameter_list|,
name|int
name|resultSetConcurrency
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|,
name|resultSetType
argument_list|,
name|resultSetConcurrency
argument_list|)
return|;
block|}
specifier|public
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|int
name|autoGeneratedKeys
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|,
name|autoGeneratedKeys
argument_list|)
return|;
block|}
specifier|public
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|int
index|[]
name|columnIndexes
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|,
name|columnIndexes
argument_list|)
return|;
block|}
specifier|public
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|String
index|[]
name|columnNames
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|,
name|columnNames
argument_list|)
return|;
block|}
specifier|public
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|void
name|releaseSavepoint
parameter_list|(
name|Savepoint
name|savepoint
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|releaseSavepoint
argument_list|(
name|savepoint
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|rollback
parameter_list|()
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|rollback
parameter_list|(
name|Savepoint
name|savepoint
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|rollback
argument_list|(
name|savepoint
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAutoCommit
parameter_list|(
name|boolean
name|autoCommit
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|setAutoCommit
argument_list|(
name|autoCommit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCatalog
parameter_list|(
name|String
name|catalog
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setHoldability
parameter_list|(
name|int
name|holdability
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|setHoldability
argument_list|(
name|holdability
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setReadOnly
parameter_list|(
name|boolean
name|readOnly
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|setReadOnly
argument_list|(
name|readOnly
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Savepoint
name|setSavepoint
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|setSavepoint
argument_list|()
return|;
block|}
specifier|public
name|Savepoint
name|setSavepoint
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|connection
operator|.
name|setSavepoint
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTransactionIsolation
parameter_list|(
name|int
name|level
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|setTransactionIsolation
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTypeMap
parameter_list|(
name|Map
name|arg0
parameter_list|)
throws|throws
name|SQLException
block|{
name|connection
operator|.
name|setTypeMap
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

