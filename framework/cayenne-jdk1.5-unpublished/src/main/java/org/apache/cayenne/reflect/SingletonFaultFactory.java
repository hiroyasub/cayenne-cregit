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
name|access
operator|.
name|ToManyListFault
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
name|access
operator|.
name|ToManyMapFault
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
name|access
operator|.
name|ToManySetFault
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
name|access
operator|.
name|ToOneFault
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|SingletonFaultFactory
implements|implements
name|FaultFactory
block|{
specifier|protected
name|Fault
name|toOneFault
init|=
operator|new
name|ToOneFault
argument_list|()
decl_stmt|;
specifier|protected
name|Fault
name|listFault
init|=
operator|new
name|ToManyListFault
argument_list|()
decl_stmt|;
specifier|protected
name|Fault
name|setFault
init|=
operator|new
name|ToManySetFault
argument_list|()
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|Accessor
argument_list|,
name|Fault
argument_list|>
name|mapFaults
init|=
operator|new
name|HashMap
argument_list|<
name|Accessor
argument_list|,
name|Fault
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Fault
name|getCollectionFault
parameter_list|()
block|{
return|return
name|listFault
return|;
block|}
specifier|public
name|Fault
name|getListFault
parameter_list|()
block|{
return|return
name|listFault
return|;
block|}
specifier|public
name|Fault
name|getMapFault
parameter_list|(
name|Accessor
name|mapKeyAccessor
parameter_list|)
block|{
synchronized|synchronized
init|(
name|mapFaults
init|)
block|{
name|Fault
name|fault
init|=
name|mapFaults
operator|.
name|get
argument_list|(
name|mapKeyAccessor
argument_list|)
decl_stmt|;
if|if
condition|(
name|fault
operator|==
literal|null
condition|)
block|{
name|fault
operator|=
operator|new
name|ToManyMapFault
argument_list|(
name|mapKeyAccessor
argument_list|)
expr_stmt|;
name|mapFaults
operator|.
name|put
argument_list|(
name|mapKeyAccessor
argument_list|,
name|fault
argument_list|)
expr_stmt|;
block|}
return|return
name|fault
return|;
block|}
block|}
specifier|public
name|Fault
name|getSetFault
parameter_list|()
block|{
return|return
name|setFault
return|;
block|}
specifier|public
name|Fault
name|getToOneFault
parameter_list|()
block|{
return|return
name|toOneFault
return|;
block|}
block|}
end_class

end_unit

