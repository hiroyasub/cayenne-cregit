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
name|reflect
operator|.
name|generic
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
name|Fault
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
name|ObjRelationship
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
name|PropertyException
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
name|PropertyVisitor
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
name|ToOneProperty
import|;
end_import

begin_comment
comment|/**  * An ArcProperty for accessing to-one relationships.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|DataObjectToOneProperty
extends|extends
name|DataObjectBaseProperty
implements|implements
name|ToOneProperty
block|{
specifier|protected
name|ObjRelationship
name|relationship
decl_stmt|;
specifier|protected
name|String
name|reverseName
decl_stmt|;
specifier|protected
name|ClassDescriptor
name|targetDescriptor
decl_stmt|;
specifier|protected
name|Fault
name|fault
decl_stmt|;
name|DataObjectToOneProperty
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|ClassDescriptor
name|targetDescriptor
parameter_list|,
name|Fault
name|fault
parameter_list|)
block|{
name|this
operator|.
name|relationship
operator|=
name|relationship
expr_stmt|;
name|this
operator|.
name|targetDescriptor
operator|=
name|targetDescriptor
expr_stmt|;
name|this
operator|.
name|reverseName
operator|=
name|relationship
operator|.
name|getReverseRelationshipName
argument_list|()
expr_stmt|;
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
specifier|public
name|ArcProperty
name|getComplimentaryReverseArc
parameter_list|()
block|{
return|return
name|reverseName
operator|!=
literal|null
condition|?
operator|(
name|ArcProperty
operator|)
name|targetDescriptor
operator|.
name|getProperty
argument_list|(
name|reverseName
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|ClassDescriptor
name|getTargetDescriptor
parameter_list|()
block|{
return|return
name|targetDescriptor
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|relationship
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|ObjRelationship
name|getRelationship
parameter_list|()
block|{
return|return
name|relationship
return|;
block|}
specifier|public
name|void
name|injectValueHolder
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|,
name|boolean
name|setReverse
parameter_list|)
block|{
try|try
block|{
name|toDataObject
argument_list|(
name|source
argument_list|)
operator|.
name|setToOneTarget
argument_list|(
name|getName
argument_list|()
argument_list|,
name|toDataObject
argument_list|(
name|target
argument_list|)
argument_list|,
name|setReverse
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Error setting to-one DataObject property: "
operator|+
name|getName
argument_list|()
argument_list|,
name|this
argument_list|,
name|source
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|visit
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitToOne
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isFault
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
operator|instanceof
name|Fault
return|;
block|}
specifier|public
name|void
name|invalidate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

