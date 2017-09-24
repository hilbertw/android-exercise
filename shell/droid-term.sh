# Script to start "input" on the device, which has a very rudimentary
# shell.
#
base=/sdcard
export CLASSPATH=$base/droid-term.jar
exec app_process $base com.droidterm.Input $*

