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
name|exp
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
name|ObjEntity
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
name|RemoteCayenneCase
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
name|ClientMtTable1Subclass
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
name|MtTable1Subclass
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
literal|"cayenne-multi-tier.xml"
argument_list|)
specifier|public
class|class
name|ValueInjectorTest
extends|extends
name|RemoteCayenneCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
specifier|public
name|void
name|test
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|MtTable1Subclass
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|qualifier
init|=
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
decl_stmt|;
try|try
block|{
name|MtTable1Subclass
name|ee
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MtTable1Subclass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ee
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|,
literal|"sub1"
argument_list|)
expr_stmt|;
comment|//check AND
name|entity
operator|.
name|setDeclaredQualifier
argument_list|(
name|qualifier
operator|.
name|andExp
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"serverAttribute1 = 'sa'"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ee
operator|=
name|context
operator|.
name|newObject
argument_list|(
name|MtTable1Subclass
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ee
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|,
literal|"sub1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ee
operator|.
name|getServerAttribute1
argument_list|()
argument_list|,
literal|"sa"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|entity
operator|.
name|setDeclaredQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testRemote
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createROPContext
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|ClientMtTable1Subclass
operator|.
name|class
argument_list|)
decl_stmt|;
name|Expression
name|qualifier
init|=
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
decl_stmt|;
try|try
block|{
name|ClientMtTable1Subclass
name|ee
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1Subclass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ee
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|,
literal|"sub1"
argument_list|)
expr_stmt|;
comment|//check AND
name|entity
operator|.
name|setDeclaredQualifier
argument_list|(
name|qualifier
operator|.
name|andExp
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"serverAttribute1 = 'sa'"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ee
operator|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1Subclass
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ee
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|,
literal|"sub1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ee
operator|.
name|getServerAttribute1
argument_list|()
argument_list|,
literal|"sa"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|entity
operator|.
name|setDeclaredQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

