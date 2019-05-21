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
name|reflect
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
name|map
operator|.
name|ObjAttribute
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
name|query
operator|.
name|EntityResultSegment
import|;
end_import

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

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|PersistentDescriptorResultMetadata
implements|implements
name|EntityResultSegment
block|{
name|ClassDescriptor
name|classDescriptor
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reverseFields
decl_stmt|;
name|PersistentDescriptorResultMetadata
parameter_list|(
name|ClassDescriptor
name|classDescriptor
parameter_list|)
block|{
name|this
operator|.
name|classDescriptor
operator|=
name|classDescriptor
expr_stmt|;
name|this
operator|.
name|fields
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|reverseFields
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|classDescriptor
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
specifier|public
name|String
name|getColumnPath
parameter_list|(
name|String
name|resultSetLabel
parameter_list|)
block|{
return|return
name|reverseFields
operator|.
name|get
argument_list|(
name|resultSetLabel
argument_list|)
return|;
block|}
name|void
name|addObjectField
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|String
name|column
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|classDescriptor
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
name|entity
operator|.
name|getAttribute
argument_list|(
name|attributeName
argument_list|)
decl_stmt|;
name|fields
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|reverseFields
operator|.
name|put
argument_list|(
name|column
argument_list|,
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Adds a result set column mapping for a single object property of a 	 * specified entity that may differ from the root entity if inheritance is 	 * involved. 	 */
name|void
name|addObjectField
parameter_list|(
name|String
name|entityName
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|String
name|column
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|classDescriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
name|entity
operator|.
name|getAttribute
argument_list|(
name|attributeName
argument_list|)
decl_stmt|;
name|fields
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|reverseFields
operator|.
name|put
argument_list|(
name|column
argument_list|,
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Adds a result set column mapping for a single DbAttribute. 	 */
name|void
name|addDbField
parameter_list|(
name|String
name|dbAttributeName
parameter_list|,
name|String
name|column
parameter_list|)
block|{
name|fields
operator|.
name|put
argument_list|(
name|dbAttributeName
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|reverseFields
operator|.
name|put
argument_list|(
name|column
argument_list|,
name|dbAttributeName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getColumnOffset
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Column offset only makes sense in the context of a query"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

