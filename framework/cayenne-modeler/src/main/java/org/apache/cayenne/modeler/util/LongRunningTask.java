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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFrame
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JProgressBar
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Timer
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
name|CayenneRuntimeException
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A base class for monitoring progress of long running tasks. It can runshowing the exact  * percentage of the task progress or in "indeterminate" mode.  *<p>  *<i>Warning: If the task started via "startAndWait()", caller must ensure that she is  * not running in the Swing EventDispatchThread, otherwise an exception is thrown, as the  * EvenDispatchThread will be blocked, preventing LongRunningTask from showing progress  * dialog.</i>  *</p>  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|LongRunningTask
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LongRunningTask
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|int
name|DEFAULT_MS_TO_DECIDE_TO_POPUP
init|=
literal|500
decl_stmt|;
specifier|protected
name|ProgressDialog
name|dialog
decl_stmt|;
specifier|protected
name|JFrame
name|frame
decl_stmt|;
specifier|protected
name|String
name|title
decl_stmt|;
specifier|protected
name|Timer
name|taskPollingTimer
decl_stmt|;
specifier|protected
name|boolean
name|canceled
decl_stmt|;
specifier|protected
name|int
name|minValue
decl_stmt|;
specifier|protected
name|int
name|maxValue
decl_stmt|;
specifier|protected
name|boolean
name|finished
decl_stmt|;
specifier|public
name|LongRunningTask
parameter_list|(
name|JFrame
name|frame
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|frame
operator|=
name|frame
expr_stmt|;
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
comment|/**      * Starts current task, and blocks current thread until the task is done.      */
specifier|public
specifier|synchronized
name|void
name|startAndWait
parameter_list|()
block|{
comment|// running from Event Dispatch Thread is bad, as this will block the timers...
if|if
condition|(
name|SwingUtilities
operator|.
name|isEventDispatchThread
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't block EventDispatchThread. Call 'startAndWait' from another thread."
argument_list|)
throw|;
block|}
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|finished
condition|)
block|{
return|return;
block|}
try|try
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|setCanceled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|notifyAll
argument_list|()
expr_stmt|;
block|}
comment|/**      * Configures the task to run in a separate thread, and immediately exits the method.      * This method is allowed to be invoked from EventDispatchThread.      */
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// prepare...
name|setCanceled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|finished
operator|=
literal|false
expr_stmt|;
name|Thread
name|task
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|internalExecute
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Timer
name|progressDisplayTimer
init|=
operator|new
name|Timer
argument_list|(
name|DEFAULT_MS_TO_DECIDE_TO_POPUP
argument_list|,
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|showProgress
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|progressDisplayTimer
operator|.
name|setRepeats
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// start
name|progressDisplayTimer
operator|.
name|start
argument_list|()
expr_stmt|;
name|task
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Starts progress dialog if the task is not finished yet.      */
specifier|protected
specifier|synchronized
name|void
name|showProgress
parameter_list|()
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"will show progress..."
argument_list|)
expr_stmt|;
if|if
condition|(
name|finished
condition|)
block|{
return|return;
block|}
name|int
name|currentValue
init|=
name|getCurrentValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isCanceled
argument_list|()
operator|&&
name|currentValue
operator|<
name|getMaxValue
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"task still in progress, will show progress dialog..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|dialog
operator|=
operator|new
name|ProgressDialog
argument_list|(
name|frame
argument_list|,
literal|"Progress..."
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|this
operator|.
name|dialog
operator|.
name|getCancelButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|setCanceled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|getProgressBar
argument_list|()
operator|.
name|setMinimum
argument_list|(
name|getMinValue
argument_list|()
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|getProgressBar
argument_list|()
operator|.
name|setMaximum
argument_list|(
name|getMaxValue
argument_list|()
argument_list|)
expr_stmt|;
name|updateProgress
argument_list|()
expr_stmt|;
name|this
operator|.
name|taskPollingTimer
operator|=
operator|new
name|Timer
argument_list|(
literal|500
argument_list|,
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|updateProgress
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|taskPollingTimer
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates current state of the progress dialog.      */
specifier|protected
name|void
name|updateProgress
parameter_list|()
block|{
if|if
condition|(
name|isCanceled
argument_list|()
condition|)
block|{
name|stop
argument_list|()
expr_stmt|;
return|return;
block|}
name|dialog
operator|.
name|getStatusLabel
argument_list|()
operator|.
name|setText
argument_list|(
name|getCurrentNote
argument_list|()
argument_list|)
expr_stmt|;
name|JProgressBar
name|progressBar
init|=
name|dialog
operator|.
name|getProgressBar
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isIndeterminate
argument_list|()
condition|)
block|{
name|progressBar
operator|.
name|setValue
argument_list|(
name|getCurrentValue
argument_list|()
argument_list|)
expr_stmt|;
name|progressBar
operator|.
name|setIndeterminate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|progressBar
operator|.
name|setIndeterminate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|synchronized
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|taskPollingTimer
operator|!=
literal|null
condition|)
block|{
name|taskPollingTimer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|dialog
operator|!=
literal|null
condition|)
block|{
name|dialog
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
comment|// there maybe other threads waiting on this task to finish...
name|finished
operator|=
literal|true
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFinished
parameter_list|()
block|{
return|return
name|finished
return|;
block|}
specifier|public
name|boolean
name|isCanceled
parameter_list|()
block|{
return|return
name|canceled
return|;
block|}
specifier|public
name|void
name|setCanceled
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"task canceled"
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|canceled
operator|=
name|b
expr_stmt|;
block|}
specifier|protected
name|void
name|internalExecute
parameter_list|()
block|{
try|try
block|{
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|setCanceled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|logObj
operator|.
name|warn
argument_list|(
literal|"task error"
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Runs the actual task, consulting "isCanceled()" state to make sure that the task      * wasn't canceled.      */
specifier|protected
specifier|abstract
name|void
name|execute
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|String
name|getCurrentNote
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|int
name|getCurrentValue
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|boolean
name|isIndeterminate
parameter_list|()
function_decl|;
specifier|public
name|int
name|getMaxValue
parameter_list|()
block|{
return|return
name|maxValue
return|;
block|}
specifier|public
name|void
name|setMaxValue
parameter_list|(
name|int
name|maxValue
parameter_list|)
block|{
name|this
operator|.
name|maxValue
operator|=
name|maxValue
expr_stmt|;
block|}
specifier|public
name|int
name|getMinValue
parameter_list|()
block|{
return|return
name|minValue
return|;
block|}
specifier|public
name|void
name|setMinValue
parameter_list|(
name|int
name|minValue
parameter_list|)
block|{
name|this
operator|.
name|minValue
operator|=
name|minValue
expr_stmt|;
block|}
block|}
end_class

end_unit

