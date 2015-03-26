#include "edu_uco_sdd_spring15_dj_drmr_record_Lame.h"
#include "simple_lame_lib.h"

JNIEXPORT void JNICALL Java_edu_uco_sdd_spring15_dj_1drmr_record_Lame_log
  (JNIEnv *env, jclass cls, jboolean on) {
	simple_lame_lib_log(on);
}
