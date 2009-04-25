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
name|gen
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|*
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
name|BasicCase
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
name|query
operator|.
name|NamedQuery
import|;
end_import

begin_class
specifier|public
class|class
name|ClassGenerationActionTest
extends|extends
name|BasicCase
block|{
specifier|protected
name|ClassGenerationAction
name|action
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|StringWriter
argument_list|>
name|writers
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|writers
operator|=
operator|new
name|ArrayList
argument_list|<
name|StringWriter
argument_list|>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|this
operator|.
name|action
operator|=
operator|new
name|ClassGenerationAction
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Writer
name|openWriter
parameter_list|(
name|TemplateType
name|templateType
parameter_list|)
throws|throws
name|Exception
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|writers
operator|.
name|add
argument_list|(
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|action
operator|=
literal|null
expr_stmt|;
name|writers
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteArtifactPairsImports
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|testEntity1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"TE1"
argument_list|)
decl_stmt|;
name|testEntity1
operator|.
name|setClassName
argument_list|(
literal|"org.example.TestClass1"
argument_list|)
expr_stmt|;
name|action
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|action
operator|.
name|setSuperPkg
argument_list|(
literal|"org.example.auto"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|generated
init|=
name|execute
argument_list|(
operator|new
name|EntityArtifact
argument_list|(
name|testEntity1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|generated
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|superclass
init|=
name|generated
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"package org.example.auto;"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"import org.apache.cayenne.CayenneDataObject;"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|subclass
init|=
name|generated
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|subclass
argument_list|,
name|subclass
operator|.
name|contains
argument_list|(
literal|"package org.example;"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subclass
argument_list|,
name|subclass
operator|.
name|contains
argument_list|(
literal|"import org.example.auto._TestClass1;"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteArtifactPairsMapRelationships
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|testEntity1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"TE1"
argument_list|)
decl_stmt|;
name|testEntity1
operator|.
name|setClassName
argument_list|(
literal|"org.example.TestClass1"
argument_list|)
expr_stmt|;
specifier|final
name|ObjEntity
name|testEntity2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"TE1"
argument_list|)
decl_stmt|;
name|testEntity2
operator|.
name|setClassName
argument_list|(
literal|"org.example.TestClass2"
argument_list|)
expr_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"xMap"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isToMany
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Entity
name|getTargetEntity
parameter_list|()
block|{
return|return
name|testEntity2
return|;
block|}
block|}
decl_stmt|;
name|relationship
operator|.
name|setCollectionType
argument_list|(
literal|"java.util.Map"
argument_list|)
expr_stmt|;
name|testEntity1
operator|.
name|addRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
name|action
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|generated
init|=
name|execute
argument_list|(
operator|new
name|EntityArtifact
argument_list|(
name|testEntity1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|generated
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|superclass
init|=
name|generated
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"import java.util.Map;"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteArtifactPairsAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|testEntity1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"TE1"
argument_list|)
decl_stmt|;
name|testEntity1
operator|.
name|setClassName
argument_list|(
literal|"org.example.TestClass1"
argument_list|)
expr_stmt|;
name|ObjAttribute
name|attr
init|=
operator|new
name|ObjAttribute
argument_list|()
decl_stmt|;
name|attr
operator|.
name|setName
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setType
argument_list|(
literal|"int"
argument_list|)
expr_stmt|;
name|ObjAttribute
name|attr1
init|=
operator|new
name|ObjAttribute
argument_list|()
decl_stmt|;
name|attr1
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|attr1
operator|.
name|setType
argument_list|(
literal|"char"
argument_list|)
expr_stmt|;
name|testEntity1
operator|.
name|addAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|testEntity1
operator|.
name|addAttribute
argument_list|(
name|attr1
argument_list|)
expr_stmt|;
name|action
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|generated
init|=
name|execute
argument_list|(
operator|new
name|EntityArtifact
argument_list|(
name|testEntity1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|generated
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|superclass
init|=
name|generated
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"public void setID(int ID)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"writeProperty(\"ID\", ID);"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"public int getID()"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"Object value = readProperty(\"ID\");"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"return (value != null) ? (Integer) value : 0;"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"public void setName(char name)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"writeProperty(\"name\", name);"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"public char getName()"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"Object value = readProperty(\"name\");"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"return (value != null) ? (Character) value : 0;"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteDataMapQueryNames
parameter_list|()
throws|throws
name|Exception
block|{
name|runDataMapTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecuteClientDataMapQueryNames
parameter_list|()
throws|throws
name|Exception
block|{
name|runDataMapTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runDataMapTest
parameter_list|(
name|boolean
name|client
parameter_list|)
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addQuery
argument_list|(
operator|new
name|NamedQuery
argument_list|(
literal|"TestQuery"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|setName
argument_list|(
literal|"testmap"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|generated
decl_stmt|;
if|if
condition|(
name|client
condition|)
block|{
name|map
operator|.
name|setDefaultClientPackage
argument_list|(
literal|"testpackage"
argument_list|)
expr_stmt|;
name|generated
operator|=
name|execute
argument_list|(
operator|new
name|ClientDataMapArtifact
argument_list|(
name|map
argument_list|,
name|map
operator|.
name|getQueries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|setDefaultPackage
argument_list|(
literal|"testpackage"
argument_list|)
expr_stmt|;
name|generated
operator|=
name|execute
argument_list|(
operator|new
name|DataMapArtifact
argument_list|(
name|map
argument_list|,
name|map
operator|.
name|getQueries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|generated
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generated
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|contains
argument_list|(
literal|"public static final String TEST_QUERY_QUERYNAME = \"TestQuery\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCallbackMethodGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCallbacks
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClientCallbackMethodGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCallbacks
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertCallbacks
parameter_list|(
name|boolean
name|isClient
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjEntity
name|testEntity1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"TE1"
argument_list|)
decl_stmt|;
name|testEntity1
operator|.
name|setClassName
argument_list|(
literal|"org.example.TestClass1"
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|CallbackDescriptor
name|cb
range|:
name|testEntity1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getCallbacks
argument_list|()
control|)
block|{
name|cb
operator|.
name|addCallbackMethod
argument_list|(
literal|"cb"
operator|+
name|i
operator|++
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isClient
condition|)
block|{
name|action
operator|=
operator|new
name|ClientClassGenerationAction
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Writer
name|openWriter
parameter_list|(
name|TemplateType
name|templateType
parameter_list|)
throws|throws
name|Exception
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|writers
operator|.
name|add
argument_list|(
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
return|;
block|}
block|}
expr_stmt|;
block|}
name|action
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|generated
init|=
name|execute
argument_list|(
operator|new
name|EntityArtifact
argument_list|(
name|testEntity1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|generated
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|superclass
init|=
name|generated
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"public abstract class _TestClass1"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|i
condition|;
name|j
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|superclass
argument_list|,
name|superclass
operator|.
name|contains
argument_list|(
literal|"protected abstract void cb"
operator|+
name|j
operator|+
literal|"();"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|subclass
init|=
name|generated
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|i
condition|;
name|j
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|subclass
argument_list|,
name|subclass
operator|.
name|contains
argument_list|(
literal|"protected void cb"
operator|+
name|j
operator|+
literal|"() {"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|execute
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|action
operator|.
name|execute
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|strings
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|writers
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|StringWriter
name|writer
range|:
name|writers
control|)
block|{
name|strings
operator|.
name|add
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|strings
return|;
block|}
block|}
end_class

end_unit

