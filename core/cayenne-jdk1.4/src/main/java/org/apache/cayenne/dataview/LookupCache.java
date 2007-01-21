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
name|dataview
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
name|Iterator
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
name|DataObject
import|;
end_import

begin_comment
comment|/**  * @since 1.1  * @author Andriy Shapochka  */
end_comment

begin_class
specifier|public
class|class
name|LookupCache
block|{
specifier|private
name|Map
name|fieldCache
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Object
index|[]
name|EMPTY_ARRAY
init|=
operator|new
name|Object
index|[]
block|{}
decl_stmt|;
specifier|public
name|LookupCache
parameter_list|()
block|{
block|}
specifier|public
name|void
name|cache
parameter_list|(
name|ObjEntityViewField
name|field
parameter_list|,
name|List
name|dataObjects
parameter_list|)
block|{
name|Lookup
name|lookup
init|=
name|getLookup
argument_list|(
name|field
argument_list|)
decl_stmt|;
if|if
condition|(
name|lookup
operator|==
literal|null
condition|)
block|{
name|lookup
operator|=
operator|new
name|Lookup
argument_list|()
expr_stmt|;
name|fieldCache
operator|.
name|put
argument_list|(
name|field
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
block|}
name|lookup
operator|.
name|cache
argument_list|(
name|field
argument_list|,
name|dataObjects
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|fieldCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|removeFromCache
parameter_list|(
name|ObjEntityViewField
name|field
parameter_list|)
block|{
return|return
name|fieldCache
operator|.
name|remove
argument_list|(
name|field
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|Object
index|[]
name|getCachedValues
parameter_list|(
name|ObjEntityViewField
name|field
parameter_list|)
block|{
name|Lookup
name|lookup
init|=
name|getLookup
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|Object
index|[]
name|values
init|=
operator|(
name|lookup
operator|!=
literal|null
condition|?
name|lookup
operator|.
name|values
else|:
name|EMPTY_ARRAY
operator|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|length
operator|==
literal|0
condition|)
return|return
name|values
return|;
else|else
block|{
name|Object
index|[]
name|valuesCopy
init|=
operator|new
name|Object
index|[
name|values
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|values
argument_list|,
literal|0
argument_list|,
name|valuesCopy
argument_list|,
literal|0
argument_list|,
name|values
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|valuesCopy
return|;
block|}
block|}
specifier|public
name|DataObject
name|getDataObject
parameter_list|(
name|ObjEntityViewField
name|field
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Lookup
name|lookup
init|=
name|getLookup
argument_list|(
name|field
argument_list|)
decl_stmt|;
if|if
condition|(
name|lookup
operator|==
literal|null
condition|)
return|return
literal|null
return|;
return|return
name|lookup
operator|.
name|getDataObject
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|private
name|Lookup
name|getLookup
parameter_list|(
name|ObjEntityViewField
name|field
parameter_list|)
block|{
if|if
condition|(
name|field
operator|==
literal|null
condition|)
return|return
literal|null
return|;
return|return
operator|(
name|Lookup
operator|)
name|fieldCache
operator|.
name|get
argument_list|(
name|field
argument_list|)
return|;
block|}
specifier|private
class|class
name|Lookup
block|{
name|ObjEntityViewField
name|field
decl_stmt|;
name|Object
index|[]
name|values
init|=
name|EMPTY_ARRAY
decl_stmt|;
name|Map
name|valueDataObjectMap
decl_stmt|;
name|void
name|cache
parameter_list|(
name|ObjEntityViewField
name|field
parameter_list|,
name|List
name|dataObjects
parameter_list|)
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
if|if
condition|(
name|values
operator|.
name|length
operator|!=
name|dataObjects
operator|.
name|size
argument_list|()
condition|)
name|values
operator|=
operator|new
name|Object
index|[
name|dataObjects
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|valueDataObjectMap
operator|=
operator|new
name|HashMap
argument_list|(
name|values
operator|.
name|length
operator|+
literal|1
argument_list|)
expr_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|dataObjects
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DataObject
name|item
init|=
operator|(
name|DataObject
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|values
index|[
name|index
index|]
operator|=
name|field
operator|.
name|getValue
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|valueDataObjectMap
operator|.
name|put
argument_list|(
name|values
index|[
name|index
index|]
argument_list|,
name|item
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
block|}
name|DataObject
name|getDataObject
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
operator|(
name|DataObject
operator|)
name|valueDataObjectMap
operator|.
name|get
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

