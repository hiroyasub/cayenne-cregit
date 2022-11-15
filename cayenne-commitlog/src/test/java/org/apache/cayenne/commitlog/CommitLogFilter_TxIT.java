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
name|commitlog
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|commitlog
operator|.
name|db
operator|.
name|AuditLog
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
name|commitlog
operator|.
name|db
operator|.
name|Auditable2
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
name|commitlog
operator|.
name|model
operator|.
name|ObjectChange
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
name|commitlog
operator|.
name|unit
operator|.
name|AuditableServerCase
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
name|ServerRuntimeBuilder
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
name|tx
operator|.
name|BaseTransaction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|CommitLogFilter_TxIT
extends|extends
name|AuditableServerCase
block|{
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|protected
name|CommitLogListener
name|listener
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|ServerRuntimeBuilder
name|configureCayenne
parameter_list|()
block|{
name|this
operator|.
name|listener
operator|=
parameter_list|(
name|originatingContext
parameter_list|,
name|changes
parameter_list|)
lambda|->
block|{
comment|// assert we are inside transaction
name|assertNotNull
argument_list|(
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectChange
name|c
range|:
name|changes
operator|.
name|getUniqueChanges
argument_list|()
control|)
block|{
name|AuditLog
name|log
init|=
name|runtime
operator|.
name|newContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|AuditLog
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|setLog
argument_list|(
literal|"DONE: "
operator|+
name|c
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
return|return
name|super
operator|.
name|configureCayenne
argument_list|()
operator|.
name|addModule
argument_list|(
name|b
lambda|->
name|CommitLogModule
operator|.
name|extend
argument_list|(
name|b
argument_list|)
operator|.
name|commitLogAnnotationEntitiesOnly
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|this
operator|.
name|context
operator|=
name|runtime
operator|.
name|newContext
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitLog
parameter_list|()
throws|throws
name|SQLException
block|{
name|Auditable2
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Auditable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setCharProperty1
argument_list|(
literal|"yy"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setCharProperty2
argument_list|(
literal|"zz"
argument_list|)
expr_stmt|;
name|Auditable2
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Auditable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setCharProperty1
argument_list|(
literal|"yy"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setCharProperty2
argument_list|(
literal|"zz"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|logs
init|=
name|auditLog
operator|.
name|selectAll
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

