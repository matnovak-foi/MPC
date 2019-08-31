MPC (Version 1.0), Copyright (c) 2016-2019 Matija Novak (matnovak@foi.hr).

MPC is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

MPC is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MPC.  If not, see <https://www.gnu.org/licenses/>.

The MPC system was built as part of PhD thesis entitled 
"Effect of source-code preprocessing techniquers on plagiarsim detection accuracy in 
student programming assignments"
                                                       

The MPC system eases the evaluation of detection tools so that it automates the underlying process of
plagiarism detection.

The MPC system supports the process by preparing the data for the detection, giving the possibility to use 
preprocessing techniques, enabling the usage of multiple tools, visualizing the
results in a unified form for all tools, and calculating the F-beta and other statistics needed for 
analysis phase based on the detection results. In addition the MPC system has integrated the
calibration method, described in detail in Section 4.4, it supports the process of manual marking of 
plagiarised matches, enables creation of new combinations of preprocessing techniques, and
it has the possibility for simple evaluation of preprocessing techniques. The MPC system can also be 
used as a simple plagiarism detection system by teachers which will run the detection
on multiple tools and perform the desired preprocessing techniques.

The MPC system has four working modes where the main mode is the detection mode which goes through all 
phases of the plagiarism detection process. The other three modes are: 1) calibration mode – used for 
calibration in Section 4.4, 2) technique selection mode – used for technique selection test in Section 7.6, 3) 
statistics mode – used for calculating statistics that are
input for the analysis phase (Chapter 8). The overall architecture of the system is presented in Figure 3.3, 
and in Appendix C a detailed description of the architecture is given.

SIM avalible at: https://dickgrune.com/Programs/similarity_tester/
Spector avalible at: https://gitlab.com/vimino/spector/tree/master
JPlag avilible at: https://github.com/jplag originaly from https://jplag.ipd.kit.edu/
Sherlock avalible at: https://warwick.ac.uk/fac/sci/dcs/research/ias/software/sherlock