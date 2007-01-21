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
name|jpa
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
name|reflect
operator|.
name|FieldAccessor
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

begin_class
class|class
name|CjpaCollectionFieldAccessor
extends|extends
name|FieldAccessor
block|{
specifier|public
name|CjpaCollectionFieldAccessor
parameter_list|(
name|Class
name|objectClass
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
name|propertyType
parameter_list|)
block|{
name|super
argument_list|(
name|objectClass
argument_list|,
name|propertyName
argument_list|,
name|propertyType
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Persistent
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|objectClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only supports persistent classes. Got: "
operator|+
name|objectClass
argument_list|)
throw|;
block|}
block|}
comment|/**      * Resolves a fault before setting the field.      */
annotation|@
name|Override
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
block|{
if|if
condition|(
name|newValue
operator|instanceof
name|Fault
condition|)
block|{
name|newValue
operator|=
operator|(
operator|(
name|Fault
operator|)
name|newValue
operator|)
operator|.
name|resolveFault
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

