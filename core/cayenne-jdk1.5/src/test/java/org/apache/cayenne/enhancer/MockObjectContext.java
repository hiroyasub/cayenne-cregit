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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataChannel
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
name|DeleteDenyException
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
name|ObjectId
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
name|Persistent
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
name|QueryResponse
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
name|graph
operator|.
name|GraphManager
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
name|query
operator|.
name|Query
import|;
end_import

begin_class
specifier|public
class|class
name|MockObjectContext
implements|implements
name|ObjectContext
block|{
specifier|public
name|void
name|prepareForAccess
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|,
name|boolean
name|lazyFaulting
parameter_list|)
block|{
block|}
comment|/**      * @deprecated since 3.0      */
specifier|public
name|void
name|prepareForAccess
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|)
block|{
block|}
specifier|public
name|void
name|propertyChanged
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
block|}
specifier|public
name|void
name|commitChanges
parameter_list|()
block|{
block|}
specifier|public
name|void
name|commitChangesToParent
parameter_list|()
block|{
block|}
specifier|public
name|void
name|deleteObject
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|DeleteDenyException
block|{
block|}
specifier|public
name|Collection
name|deletedObjects
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|GraphManager
name|getGraphManager
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Persistent
name|localObject
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|Object
name|prototype
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
name|modifiedObjects
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Persistent
name|newObject
parameter_list|(
name|Class
name|persistentClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
name|newObjects
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QueryResponse
name|performGenericQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
name|performQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|registerNewObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
specifier|public
name|void
name|rollbackChanges
parameter_list|()
block|{
block|}
specifier|public
name|void
name|rollbackChangesLocally
parameter_list|()
block|{
block|}
specifier|public
name|Collection
name|uncommittedObjects
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

