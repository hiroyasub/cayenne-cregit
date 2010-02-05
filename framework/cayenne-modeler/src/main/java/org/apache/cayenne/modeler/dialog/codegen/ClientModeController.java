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
name|modeler
operator|.
name|dialog
operator|.
name|codegen
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
name|gen
operator|.
name|ClassGenerationAction
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
name|gen
operator|.
name|ClientClassGenerationAction
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
name|modeler
operator|.
name|pref
operator|.
name|DataMapDefaults
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
name|validation
operator|.
name|BeanValidationFailure
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Predicate
import|;
end_import

begin_class
specifier|public
class|class
name|ClientModeController
extends|extends
name|StandardModeController
block|{
specifier|protected
name|Predicate
name|checkPredicate
decl_stmt|;
specifier|public
name|ClientModeController
parameter_list|(
name|CodeGeneratorControllerBase
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkPredicate
operator|=
operator|new
name|Predicate
argument_list|()
block|{
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|ObjEntity
condition|)
block|{
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|object
decl_stmt|;
return|return
name|entity
operator|.
name|isClientAllowed
argument_list|()
operator|&&
name|getParentController
argument_list|()
operator|.
name|getProblem
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
expr_stmt|;
block|}
specifier|public
name|void
name|validateEntity
parameter_list|(
name|ValidationResult
name|validationBuffer
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
operator|!
name|entity
operator|.
name|isClientAllowed
argument_list|()
condition|)
block|{
name|validationBuffer
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
literal|"clientAllowed"
argument_list|,
literal|"Not a client entity"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|validateEntity
argument_list|(
name|validationBuffer
argument_list|,
name|entity
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|DataMapDefaults
name|createDefaults
parameter_list|()
block|{
name|DataMapDefaults
name|prefs
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getDataMapPreferences
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
argument_list|)
decl_stmt|;
name|prefs
operator|.
name|updateSuperclassPackage
argument_list|(
name|getParentController
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|preferences
operator|=
name|prefs
expr_stmt|;
return|return
name|prefs
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ClassGenerationAction
name|newGenerator
parameter_list|()
block|{
return|return
operator|new
name|ClientClassGenerationAction
argument_list|()
return|;
block|}
specifier|public
name|Predicate
name|getDefaultEntityFilter
parameter_list|()
block|{
return|return
name|checkPredicate
return|;
block|}
block|}
end_class

end_unit

