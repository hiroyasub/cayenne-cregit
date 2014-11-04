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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|ClientMtTable2
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
name|client
operator|.
name|ClientCase
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
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|ValidationException
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneContextValidationIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidate
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientMtTable1
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"G1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|resetValidation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// this one is not validating
name|ClientMtTable2
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|o2
operator|.
name|setTable1
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|isValidatedForInsert
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isValidatedForDelete
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isValidatedForUpdate
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|resetValidation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"G2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isValidatedForInsert
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isValidatedForDelete
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|isValidatedForUpdate
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|resetValidation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isValidatedForInsert
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|isValidatedForDelete
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isValidatedForUpdate
argument_list|()
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|o11
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o11
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"G1"
argument_list|)
expr_stmt|;
name|o11
operator|.
name|resetValidation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Validation failure must have prevented commit"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

