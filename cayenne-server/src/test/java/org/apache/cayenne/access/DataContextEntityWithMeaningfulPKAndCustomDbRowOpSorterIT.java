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
name|access
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
name|access
operator|.
name|flush
operator|.
name|operation
operator|.
name|DbRowOpSorter
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
name|flush
operator|.
name|operation
operator|.
name|GraphBasedDbRowOpSorter
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
name|ServerRuntime
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
name|Binder
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
name|Module
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
name|testdo
operator|.
name|meaningful_pk
operator|.
name|MeaningfulPKDep
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
name|testdo
operator|.
name|meaningful_pk
operator|.
name|MeaningfulPKTest1
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
name|testdo
operator|.
name|meaningful_pk
operator|.
name|MeaningfulPk
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|InjectExtraModules
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MEANINGFUL_PK_PROJECT
argument_list|)
annotation|@
name|InjectExtraModules
argument_list|(
name|extraModules
operator|=
block|{
name|DataContextEntityWithMeaningfulPKAndCustomDbRowOpSorterIT
operator|.
name|CustomServerCase
operator|.
name|class
block|}
argument_list|)
specifier|public
class|class
name|DataContextEntityWithMeaningfulPKAndCustomDbRowOpSorterIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testInsertDelete
parameter_list|()
block|{
name|MeaningfulPk
name|pkObj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPk
operator|.
name|class
argument_list|)
decl_stmt|;
name|pkObj
operator|.
name|setPk
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|pkObj
argument_list|)
expr_stmt|;
name|MeaningfulPk
name|pkObj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPk
operator|.
name|class
argument_list|)
decl_stmt|;
name|pkObj2
operator|.
name|setPk
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_MeaningfulPkInsertDeleteCascade
parameter_list|()
block|{
comment|// setup
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must be able to set reverse relationship
name|MeaningfulPKDep
name|dep
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep
operator|.
name|setToMeaningfulPK
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|dep
operator|.
name|setPk
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// test
name|context
operator|.
name|deleteObject
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|MeaningfulPKTest1
name|obj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj2
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj2
operator|.
name|setDescr
argument_list|(
literal|"bbb"
argument_list|)
expr_stmt|;
name|MeaningfulPKDep
name|dep2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep2
operator|.
name|setToMeaningfulPK
argument_list|(
name|obj2
argument_list|)
expr_stmt|;
name|dep2
operator|.
name|setPk
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|static
class|class
name|CustomServerCase
implements|implements
name|Module
block|{
specifier|public
name|CustomServerCase
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|DbRowOpSorter
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|GraphBasedDbRowOpSorter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

