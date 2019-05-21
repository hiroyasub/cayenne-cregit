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
name|map
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
name|reflect
operator|.
name|ArcProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|reflect
operator|.
name|ClassDescriptorFactory
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
name|reflect
operator|.
name|LazyClassDescriptorDecorator
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
name|reflect
operator|.
name|PropertyDescriptor
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
name|MtTable1
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
name|MtTable2
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
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
name|assertSame
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
name|assertTrue
import|;
end_import

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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|EntityResolverClassDescriptorIT
extends|extends
name|ServerCase
block|{
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
name|testServerDescriptorCaching
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|clearDescriptors
argument_list|()
expr_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|descriptor
argument_list|,
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|clearDescriptors
argument_list|()
expr_stmt|;
name|ClassDescriptor
name|descriptor1
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|descriptor1
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|descriptor
argument_list|,
name|descriptor1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServerDescriptorFactory
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|clearDescriptors
argument_list|()
expr_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|mock
argument_list|(
name|ClassDescriptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClassDescriptorFactory
name|factory
init|=
name|mock
argument_list|(
name|ClassDescriptorFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|factory
operator|.
name|getDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|addFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
try|try
block|{
name|ClassDescriptor
name|resolved
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolved
argument_list|)
expr_stmt|;
name|resolved
operator|=
operator|(
operator|(
name|LazyClassDescriptorDecorator
operator|)
name|resolved
operator|)
operator|.
name|getDescriptor
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|descriptor
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|removeFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArcProperties
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|clearDescriptors
argument_list|()
expr_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|PropertyDescriptor
name|p
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|MtTable1
operator|.
name|TABLE2ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|instanceof
name|ArcProperty
argument_list|)
expr_stmt|;
name|ClassDescriptor
name|target
init|=
operator|(
operator|(
name|ArcProperty
operator|)
name|p
operator|)
operator|.
name|getTargetDescriptor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"MtTable2"
argument_list|)
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
operator|(
operator|(
name|ArcProperty
operator|)
name|p
operator|)
operator|.
name|getComplimentaryReverseArc
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MtTable2
operator|.
name|TABLE1
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
operator|(
name|ArcProperty
operator|)
name|p
operator|)
operator|.
name|getComplimentaryReverseArc
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

