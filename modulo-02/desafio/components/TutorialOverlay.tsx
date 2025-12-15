import React, { useEffect, useState } from 'react';
import { X, ArrowRight, Check } from 'lucide-react';

export interface TutorialStep {
  targetRef: React.RefObject<HTMLElement | null>;
  title: string;
  description: string;
  position: 'top' | 'bottom' | 'left' | 'right';
}

interface TutorialOverlayProps {
  steps: TutorialStep[];
  currentStep: number;
  onNext: () => void;
  onSkip: () => void;
  isTyping?: boolean;
}

const TutorialOverlay: React.FC<TutorialOverlayProps> = ({ 
  steps, 
  currentStep, 
  onNext, 
  onSkip,
  isTyping 
}) => {
  const [coords, setCoords] = useState<{ top: number; left: number; width: number; height: number } | null>(null);

  useEffect(() => {
    const updatePosition = () => {
      const step = steps[currentStep];
      if (step && step.targetRef.current) {
        const rect = step.targetRef.current.getBoundingClientRect();
        // Add a little padding
        setCoords({
          top: rect.top - 8,
          left: rect.left - 8,
          width: rect.width + 16,
          height: rect.height + 16
        });
      }
    };

    updatePosition();
    window.addEventListener('resize', updatePosition);
    window.addEventListener('scroll', updatePosition, true);

    // Re-calculate after a short delay to allow DOM updates (like animations) to finish
    const timer = setTimeout(updatePosition, 300);

    return () => {
      window.removeEventListener('resize', updatePosition);
      window.removeEventListener('scroll', updatePosition, true);
      clearTimeout(timer);
    };
  }, [currentStep, steps]);

  const step = steps[currentStep];

  if (!coords || !step) return null;

  return (
    <div className="fixed inset-0 z-[100] overflow-hidden">
      {/* Dark overlay with cutout using mix-blend-mode or simple multiple divs. 
          Here using a massive box-shadow trick for the spotlight effect which is robust. */}
      <div 
        className="absolute transition-all duration-500 ease-in-out rounded-lg pointer-events-none"
        style={{
          top: coords.top,
          left: coords.left,
          width: coords.width,
          height: coords.height,
          boxShadow: '0 0 0 9999px rgba(0, 0, 0, 0.75)' 
        }}
      />
      
      {/* Border for the spotlight */}
      <div 
        className="absolute border-2 border-blue-500 rounded-lg transition-all duration-500 ease-in-out pointer-events-none animate-pulse"
        style={{
          top: coords.top,
          left: coords.left,
          width: coords.width,
          height: coords.height,
        }}
      />

      {/* Tooltip Card */}
      <div 
        className={`absolute max-w-xs w-full transition-all duration-500 ease-out`}
        style={{
          top: step.position === 'bottom' ? coords.top + coords.height + 20 : 
               step.position === 'top' ? coords.top - 180 : coords.top,
          left: step.position === 'left' ? coords.left - 340 : 
                step.position === 'right' ? coords.left + coords.width + 20 : 
                // Center align for top/bottom but ensure it doesn't go off screen
                Math.min(Math.max(20, coords.left + (coords.width / 2) - 160), window.innerWidth - 340)
        }}
      >
        <div className="bg-white dark:bg-slate-800 p-5 rounded-xl shadow-2xl border border-slate-200 dark:border-slate-700 animate-in fade-in slide-in-from-bottom-4">
          <div className="flex justify-between items-start mb-3">
            <h3 className="font-bold text-lg text-slate-900 dark:text-white">{step.title}</h3>
            <button onClick={onSkip} className="text-slate-400 hover:text-slate-900 dark:hover:text-white">
              <X size={16} />
            </button>
          </div>
          
          <p className="text-slate-600 dark:text-slate-300 text-sm mb-4 leading-relaxed">
            {step.description}
          </p>

          <div className="flex items-center justify-between mt-2">
            <div className="flex gap-1">
              {steps.map((_, i) => (
                <div 
                  key={i} 
                  className={`h-1.5 rounded-full transition-all ${i === currentStep ? 'w-6 bg-blue-600' : 'w-1.5 bg-slate-300 dark:bg-slate-600'}`} 
                />
              ))}
            </div>
            
            <button 
              onClick={onNext}
              disabled={isTyping} // Disable next while auto-typing
              className="bg-blue-600 hover:bg-blue-500 disabled:opacity-50 disabled:cursor-not-allowed text-white px-4 py-2 rounded-lg text-sm font-medium flex items-center gap-2 transition-colors"
            >
              {currentStep === steps.length - 1 ? (
                <>Finish <Check size={14} /></>
              ) : (
                <>Next <ArrowRight size={14} /></>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TutorialOverlay;