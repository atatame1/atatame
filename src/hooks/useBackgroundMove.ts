import { useEventListener } from '@vueuse/core';
import type { Ref } from "vue";

export default function (home: Ref<HTMLElement>) {
  const handleMouseMove = (e: MouseEvent) => {
      const initX=window.innerWidth/2
      const initY=window.innerHeight/2
        if (home.value) {
          const X=(e.clientX-initX)/50
          const Y=(e.clientY-initY)/50
          home.value.style.transform=`translate(${-X}px,${-Y}px)`
        }
  };

  const bindEvent = () => {
    if (!home.value) return;
    useEventListener(window, 'mousemove', handleMouseMove);        
  }

  const unBindEvent = () => {
    if (!home.value) return;
    removeEventListener('mousemove', handleMouseMove);        
  }

  return{bindEvent,unBindEvent}
}

// const {bindEvent}=useBackgroundMove(home as Ref<HTMLElement>)
// onMounted(() => {
//     bindEvent()
// });