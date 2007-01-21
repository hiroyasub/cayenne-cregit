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
name|enhancer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|commons
operator|.
name|SerialVersionUIDAdder
import|;
end_import

begin_comment
comment|/**  * A factory of enhacing visitors.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CayenneEnhancerVisitorFactory
implements|implements
name|EnhancerVisitorFactory
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|ObjEntity
argument_list|>
name|entitiesByClass
decl_stmt|;
specifier|public
name|CayenneEnhancerVisitorFactory
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|indexEntities
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|indexEntities
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
comment|// EntityResolver doesn't have an index by class name, (let alone using
comment|// "internal" class names with slashes as keys), so we have to build it
comment|// manually
name|this
operator|.
name|entitiesByClass
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ObjEntity
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|entityResolver
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|object
decl_stmt|;
name|entitiesByClass
operator|.
name|put
argument_list|(
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ClassVisitor
name|createVisitor
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassVisitor
name|out
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|entitiesByClass
operator|.
name|get
argument_list|(
name|className
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// create enhancer chain
name|PersistentInterfaceVisitor
name|e1
init|=
operator|new
name|PersistentInterfaceVisitor
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|PersistentAccessorVisitor
name|e2
init|=
operator|new
name|PersistentAccessorVisitor
argument_list|(
name|e1
argument_list|,
name|entity
argument_list|)
decl_stmt|;
comment|// this ensures that both enhanced and original classes have compatible serialized
comment|// format even if no serialVersionUID is defined by the user
name|SerialVersionUIDAdder
name|e3
init|=
operator|new
name|SerialVersionUIDAdder
argument_list|(
name|e2
argument_list|)
decl_stmt|;
return|return
name|e3
return|;
block|}
block|}
end_class

end_unit

