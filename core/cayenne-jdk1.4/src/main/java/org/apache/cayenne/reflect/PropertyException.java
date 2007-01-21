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
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * An unchecked exception thrown on errors during property access, either within a  * Accessor or a Property.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PropertyException
extends|extends
name|CayenneRuntimeException
block|{
specifier|protected
name|Property
name|property
decl_stmt|;
specifier|protected
name|Accessor
name|accessor
decl_stmt|;
specifier|protected
name|Object
name|source
decl_stmt|;
specifier|public
name|PropertyException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PropertyException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PropertyException
parameter_list|(
name|String
name|message
parameter_list|,
name|Accessor
name|accessor
parameter_list|,
name|Object
name|source
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
name|accessor
argument_list|,
name|source
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PropertyException
parameter_list|(
name|String
name|message
parameter_list|,
name|Accessor
name|accessor
parameter_list|,
name|Object
name|source
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|accessor
operator|=
name|accessor
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
specifier|public
name|PropertyException
parameter_list|(
name|String
name|message
parameter_list|,
name|Property
name|property
parameter_list|,
name|Object
name|source
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
name|property
argument_list|,
name|source
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PropertyException
parameter_list|(
name|String
name|message
parameter_list|,
name|Property
name|property
parameter_list|,
name|Object
name|source
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|property
operator|=
name|property
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
comment|/**      * Returns property descriptor that was used to access the property. It may be null.      */
specifier|public
name|Accessor
name|getAccessor
parameter_list|()
block|{
return|return
name|accessor
return|;
block|}
specifier|public
name|Property
name|getProperty
parameter_list|()
block|{
return|return
name|property
return|;
block|}
comment|/**      * Returns an object that caused an error.      */
specifier|public
name|Object
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

