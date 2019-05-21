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
operator|.
name|jdbc
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
name|types
operator|.
name|ExtendedType
import|;
end_import

begin_comment
comment|/**  * A descriptor of a result row obtained from a database.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|RowDescriptor
block|{
specifier|protected
name|ColumnDescriptor
index|[]
name|columns
decl_stmt|;
specifier|protected
name|ExtendedType
index|[]
name|converters
decl_stmt|;
comment|/**      * Creates an empty RowDescriptor. Intended mainly for testing and use by subclasses.      */
specifier|protected
name|RowDescriptor
parameter_list|()
block|{
block|}
comment|/**      * Creates a fully initialized RowDescriptor.      *       * @since 3.0      */
specifier|public
name|RowDescriptor
parameter_list|(
name|ColumnDescriptor
index|[]
name|columns
parameter_list|,
name|ExtendedType
index|[]
name|converters
parameter_list|)
block|{
name|this
operator|.
name|columns
operator|=
name|columns
expr_stmt|;
name|this
operator|.
name|converters
operator|=
name|converters
expr_stmt|;
block|}
comment|/**      * Returns a number of columns in a row.      */
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|columns
operator|.
name|length
return|;
block|}
comment|/**      * Returns column descriptors.      */
specifier|public
name|ColumnDescriptor
index|[]
name|getColumns
parameter_list|()
block|{
return|return
name|columns
return|;
block|}
comment|/**      * Returns extended types for columns.      */
specifier|public
name|ExtendedType
index|[]
name|getConverters
parameter_list|()
block|{
return|return
name|converters
return|;
block|}
block|}
end_class

end_unit

